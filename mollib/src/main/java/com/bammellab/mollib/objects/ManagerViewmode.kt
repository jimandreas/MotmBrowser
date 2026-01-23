/*
 *  Copyright 2020 Bammellab / James Andreas
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

@file:Suppress(
        "unused",
        "MemberVisibilityCanBePrivate",
        "CanBeVal",
        "UNUSED_PARAMETER",
        "CanBeParameter", "KotlinConstantConditions", "KotlinConstantConditions",
    "KotlinConstantConditions", "SameReturnValue"
)

package com.bammellab.mollib.objects

import android.app.Activity
import com.kotmol.pdbParser.AtomInformationTable
import com.kotmol.pdbParser.Molecule
import com.kotmol.pdbParser.PdbAtom
import com.kotmol.pdbParser.PdbAtom.AtomType.IS_HETATM
import timber.log.Timber

class ManagerViewmode(private val activity: Activity,
                      private val mol: Molecule
) : ViewModeCallback {


    private val atomSphere = AtomSphere(activity, mol)
    private val atomToAtomBond = SegmentAtomToAtomBond(mol)
    private val renderRibbon = RenderRibbon(mol)
    private val renderNucleic = RenderNucleic(mol)
    private val renderCaQuickLine = RenderCaAsQuickLine(mol)
    private var currentMode = 0
    private var drawMode = 0

    var displayHydrosFlag = false
    var geometrySlices = 10

    fun getMode(): Int {
        return currentMode
    }

    fun createView() {
        currentMode = VIEW_INITIAL
        nextViewMode()
    }

    fun nextViewMode() {

        if (++currentMode > VIEW_TOTAL_MODES) {
            currentMode = VIEW_INITIAL + 1
        }

        setupViewMode()
    }

    fun doViewMode(doMode: Int) {
        currentMode = doMode
        setupViewMode()
    }

    private fun setupViewMode() {
        BufferManager.resetBuffersForNextUsage()

        val atomCount = mol.atomNumberList.size
        Timber.d("atomCount = $atomCount")
        geometrySlices = 10
        if (atomCount > 20000) {
            Timber.i("Large molecule mode.  atomCount > 20000")
            BufferManager.initViewModeCallback(this)
            BufferManager.bigMoleculeFlag(true)
        } else {
            BufferManager.initViewModeCallback(null)
            BufferManager.bigMoleculeFlag(false)
            if (atomCount < 10000) {
                geometrySlices = 20
            }
        }

        doViewMode()
    }

    /**
     * called by the BufferManager when it is put into slowRenderMode
     *    This way the rendering is controlled here but the BufferManager
     *    renders each buffer immediately.
     */
    override fun slowRender() {
        doViewMode()
    }

    /**
     * called by the BufferManager in slow Render mode
     *    and a touch is processed.
     */
    override fun renderQuickLineOnTouch() {
        BufferManager.doLineMode(true)
        renderCaQuickLine.renderQuickLine()
    }

    private fun doViewMode() {

        /*
         * let's see how much memory there is to play with
         */
//        val activityManager2 = activity.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
//        val info2 = ActivityManager.MemoryInfo()
//        activityManager2.getMemoryInfo(info2)
//        val initialAvailMem = info2.threshold

        try {
            run bailout@{
                //Timber.i("doViewMode: THRESHOLD mbyte = %d", initialAvailMem / 1024 / 1024)

                BufferManager.doLineMode(false)
                when (currentMode) {
                    VIEW_RIBBONS -> {
                        drawMode = D_PIPE_RADIUS or D_NUCLEIC or D_HETATM or D_RIBBONS
                        calcMemoryUsage(drawMode)
                        clearRenderThisAtomAsSphereFlag()
                        renderRibbon.renderModal()
                        renderNucleic.renderNucleic()
                        drawNucleicBonds()
                        drawHetatmBonds()
                        drawSpheres()
                    }

                    VIEW_RIBBONS_DEV_ALL -> {
                        drawMode = (D_BALL_RADIUS or D_NUCLEIC or D_HETATM or D_ALL_ATOMS
                                or D_RIBBONS or D_BONDS or D_SPHERES)
                        /*
                         * if there isn't enough memory, then drop back into ribbon mode
                         *    for now.   Save the check for no ribbons for later
                         *    TODO:  handle the no ribbon case
                         */
                        if (!calcMemoryUsage(drawMode)) {
                            drawMode = D_PIPE_RADIUS or D_NUCLEIC or D_HETATM or D_RIBBONS
                            currentMode = VIEW_RIBBONS
                            clearRenderThisAtomAsSphereFlag()
                            renderRibbon.renderModal()
                            renderNucleic.renderNucleic()
                            drawNucleicBonds()
                            drawHetatmBonds()
                            drawSpheres()
                            // TODO: flag in UI about insufficient memory
                            // mActivity.noMemoryForAtomView();
                            // bailout of doing more rendering
                            return@bailout
                        }
                        drawPipeModel()
                        drawSpheres()
                        renderRibbon.renderModal()
                        renderNucleic.renderNucleic()
                    }

                    VIEW_STICK -> {
                        drawMode = (D_PIPE_RADIUS or D_NUCLEIC or D_HETATM or D_ALL_ATOMS
                                or D_BONDS or D_SPHERES)
                        calcMemoryUsage(drawMode)
                        clearRenderThisAtomAsSphereFlag()  // bonds will set the flag
                        drawPipeModel()
                        drawSpheres()
                    }
                    VIEW_BALL_AND_STICK -> {
                        drawMode = (D_BALL_RADIUS or D_NUCLEIC or D_HETATM or D_ALL_ATOMS
                                or D_BONDS or D_SPHERES)
                        calcMemoryUsage(drawMode)
                        clearRenderThisAtomAsSphereFlag()  // bonds will set the flag
                        drawPipeModel()
                        drawSpheres()
                    }

                    VIEW_SPHERE -> {
                        drawMode = (D_REAL_RADIUS or D_NUCLEIC or D_HETATM or D_ALL_ATOMS
                                or D_SPHERES)
                        calcMemoryUsage(drawMode)
                        setRenderThisAtomAsSphereFlag()
                        drawSpheres()
                    }
                    // not used now - retain for future debugging
                    VIEW_CA_QUICK_LINE -> {
                        BufferManager.doLineMode(true)
                        renderCaQuickLine.renderQuickLine()
                    }
                }
                BufferManager.setBufferLoadingComplete()
                Timber.v("doViewMode: buffer loading complete")
            }
        } catch (e: Exception) {
            Timber.e(e, "Crashed displaying: %s", mol.molName)
        }

    }

    /*
     * obsolete now - but
     *    it was fun while it lasted
     */
    //            case VIEW_RIBBONS_ONLY:
    //                renderAlphaHelix.renderAlphaHelices();
    //                mRenderBetaRibbon.renderBetaRibbons();
    //                mRenderRibbon.renderRibbon();
    //                // renderBackbone();
    //                break;
    //            case VIEW_RIBBONS_AND_BACKBONE:
    //                renderAlphaHelix.renderAlphaHelices();
    //                mRenderBetaRibbon.renderBetaRibbons();
    //                mRenderRibbon.renderRibbon();
    //                renderBackbone();
    //                break;
    //
    //            case VIEW_RIBBONS_PLUS_BALL_AND_STICK:
    //                drawPipeModel();
    //                drawSpheres(0.5f, DRAW_ALL);
    //                renderBackbone();
    //                break;

    // float end_time = SystemClock.uptimeMillis();
    // float elapsed_time = (end_time - start_time) / 1000;
    // String pretty_print = String.format("%6.2f", elapsed_time);


    /*
     * Draw: pipe model
     */
    private fun drawPipeModel() {

        var atom1Number: Int
        var atom2Number: Int
        var atom1: PdbAtom?
        var atom2: PdbAtom?

        for (i in 0 until mol.bondList.size) {
            atom1Number = mol.bondList[i].atomNumber1
            atom2Number = mol.bondList[i].atomNumber2

            atom1 = mol.atomNumberToAtomInfoHash[atom1Number]
            atom2 = mol.atomNumberToAtomInfoHash[atom2Number]
            if (atom1 == null || atom2 == null) {
                Timber.e("null ptr : atom1: $atom1 atom2: $atom2")
                continue
            }

            atomToAtomBond.genBondCylinders(
                    geometrySlices,
                    0.25f,
                    atom1,
                    atom2)

        }
    }

    /*
     * Draw: pipe bond model for nucleic atoms
     */
    private fun drawNucleicBonds() {

        var atom1Number: Int
        var atom2Number: Int
        var atom1: PdbAtom?
        var atom2: PdbAtom?

        for (i in 0 until mol.bondList.size) {
            atom1Number = mol.bondList[i].atomNumber1
            atom2Number = mol.bondList[i].atomNumber2

            atom1 = mol.atomNumberToAtomInfoHash[atom1Number]
            atom2 = mol.atomNumberToAtomInfoHash[atom2Number]

            if (atom1 == null || atom2 == null) {
                Timber.e("null ptr : atom1: $atom1 atom2: $atom2")
                continue
            }
            // if (!atom1.is_nucleic_atom || !atom2.is_nucleic_atom) {
            if (atom1.atomType != PdbAtom.AtomType.IS_NUCLEIC || atom2.atomType != PdbAtom.AtomType.IS_NUCLEIC) {

                continue
            }

            if (!displayHydrosFlag) {
                if (atom1.elementSymbol == "H" || atom2.elementSymbol == "H") {

                    continue
                }
            }
            atomToAtomBond.genBondCylinders(
                    geometrySlices,
                    0.25f,
                    atom1,
                    atom2)

        }
    }

    /*
     * Draw: pipe bond model for HETATMs
     *     changes for this method:
     *        if one of the atoms is a metal
     */
    private fun drawHetatmBonds() {

        var atom1Number: Int
        var atom2Number: Int
        var atom1: PdbAtom?
        var atom2: PdbAtom?

        for (i in 0 until mol.bondList.size) {
            atom1Number = mol.bondList[i].atomNumber1
            atom2Number = mol.bondList[i].atomNumber2

            atom1 = mol.atomNumberToAtomInfoHash[atom1Number]
            atom2 = mol.atomNumberToAtomInfoHash[atom2Number]

            if (atom1 == null || atom2 == null) {
                Timber.e("null ptr : atom1: $atom1 atom2: $atom2")
                continue
            }
            if (atom1.atomType != IS_HETATM && atom2.atomType != IS_HETATM) {
                continue
            }

            if (!displayHydrosFlag) {
                if (atom1.elementSymbol == "H" || atom2.elementSymbol == "H") {

                    continue
                }
            }

            atomToAtomBond.genBondCylinders(
                    geometrySlices,
                    0.25f,
                    atom1,
                    atom2)

        }
    }

    /**
     * for all the atoms - clear the flag that the atom
     * needs to be rendered as a sphere.  This flag is set if the
     * atom is at the end of a bond cylinder.
     */
    private fun clearRenderThisAtomAsSphereFlag() {
        for (i in 0 until mol.atomNumberList.size) {
            val atom = mol.atomNumberToAtomInfoHash[mol.atomNumberList[i]]
            if (atom != null) {
                atom.renderThisAtomAsSphere = false
            }
        }
    }

    private fun setRenderThisAtomAsSphereFlag() {
        for (i in 0 until mol.atomNumberList.size) {
            val atom = mol.atomNumberToAtomInfoHash[mol.atomNumberList[i]]
            if (atom != null) {
                // skip HOH (water) molecules
                atom.renderThisAtomAsSphere = !(atom.atomType == IS_HETATM && atom.residueName == "HOH")
            }
        }
    }

    /**
     * TODO: right now this method is called for both ribbon mode and molecule mode
     *
     */
    private fun drawSpheres() {

        var atom: PdbAtom?
        var elementSymbol: String
        var ai: AtomInformationTable.AtomNameNumber?
        var useColor: FloatArray
        val color = floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f)
        var lookupRadius = false
        var useRadius: Float

        /*
         * if we are in sphere mode (spacefilling) then look up the sphere radius,
         *   otherwise big spheres (for ball and stick) or stick sized to blend with stick
         *   junctions.
         */
        var radius = 0f
        when {
            drawMode and D_REAL_RADIUS == D_REAL_RADIUS -> lookupRadius = true
            drawMode and D_BALL_RADIUS == D_BALL_RADIUS -> radius = 0.50f
            else -> radius = 0.25f
        }

        for (i in 0 until mol.atomNumberList.size) {
            atom = mol.atomNumberToAtomInfoHash[mol.atomNumberList[i]]
            if (atom == null) {
                Timber.e("drawSpheres: error - got null for %d", mol.atomNumberList[i])
                continue
            }
            // skip HOH (water) molecules
            if (!atom.renderThisAtomAsSphere && atom.atomType == IS_HETATM && atom.residueName == "HOH") {
                continue
            }
            // skip TER records
            if (atom.atomName == "TER_RECORD") {
                continue
            }
//            if (atom1.atomType == PdbAtom.AtomType.IS_NUCLEIC) {
//                if (drawMode and D_NUCLEIC == 0) {
//                    continue
//                }
//            } else if (atom1.atomType == PdbAtom.AtomType.IS_HETATM) {
//                if (drawMode and D_HETATM == 0) {
//                    continue
//                }
//            } else if (atom1.elementSymbol == "H") {
//                if (!displayHydrosFlag) {
//                    continue
//                } else if (drawMode and D_ALL_ATOMS == 0) {
//                    continue
//                }
//            } else if (drawMode and D_ALL_ATOMS == 0) {
//                if (atom1.atomType != PdbAtom.AtomType.IS_HETATM) {
//                    continue
//                }
//            }
            if (atom.atomBondCount == 0) {
                if (atom.atomName != "TER_RECORD") {
                    Timber.e("%s: drawSpheres no bond at atom %d residue %s type %s",
                            mol.molName, atom.atomNumber, atom.residueName, atom.atomName)
                }
            }
            if (!atom.renderThisAtomAsSphere) {
                continue
            }
            elementSymbol = atom.elementSymbol
            ai = AtomInformationTable.atomSymboltoAtomNumNameColor[elementSymbol]
            useColor = ai?.color ?: color
            /*
             * for full sized atom mode, look up the radius
             */
            if (lookupRadius) {
                radius = if (ai == null) {
                    Timber.e("drawSpheres: no AtomInfo at atom %d residue %s type %s",
                            atom.atomNumber,
                            atom.residueName,
                            atom.atomName)
                    .25f
                } else {
                    ai.vanDerWaalsRadius.toFloat() / 100f
                }
                if (radius == 0f) {
                    radius = .25f
                }
                useRadius = radius
            } else { // ball and stick mode
                useRadius = if (elementSymbol == "H") {
                    .30f  // override for smallish hydrogens
                } else {
                    radius
                }
            }
            atomSphere.genSphere(
                    SPHERE_SLICES,
                    useRadius,
                    atom,
                    useColor)
        }
        BufferManager.transferToGl()
    }

    /*
    private void renderBackbone() {

        float color[];
        float red_color[] = new float[]{1.0f, 0.0f, 0.0f, 1.0f};
        float chimera_color[] = new float[]{229f / 256f, 196f / 256f, 153f / 256f, 1.0f};
        float blue_color[] = new float[]{0.0f, 0.0f, 1.0f, 1.0f};
        float orange_color[] = new float[]{255f / 256f, 161f / 256f, 3f / 256f, 1.0f};
        float sulfur_yellow_color[] = new float[]{255f / 256f, 161f / 256f, 3f / 256f, 1.0f};

        Vector3 v1 = new Vector3();
        Vector3 v2 = new Vector3();
        ArrayList<CatmullRomCurve> curve_list = new ArrayList<>();
        CatmullRomCurve path = null;
        List pdb_backbone_list;
        ChainRenderingDescriptor chain_entry;
        PdbAtom atom;

        for (int i = 0; i < mMol.listofChainDescriptorLists.size(); i++) {
            pdb_backbone_list = mMol.listofChainDescriptorLists.get(i);
            if (pdb_backbone_list.size() >= 4) {

                path = new CatmullRomCurve();
                // path.isClosedCurve(true);
                curve_list.add(path);
                for (int j = 0; j < pdb_backbone_list.size(); j++) {
                    chain_entry = (ChainRenderingDescriptor) pdb_backbone_list.get(j);
                    atom = chain_entry.backboneAtom;
                    if (atom == null) {
                        Timber.e("renderBackbone: null atom in list, i = " + i);
                        return;
                    }
                    // must do a NEW Vector3 one for each addPoint
                    Vector3 vnew = new Vector3(atom.atom_position3f);
                    path.addPoint(vnew);
                }
            }
        }

        for (int j = 0; j < curve_list.size(); j++) {
            path = curve_list.get(j);
            color = red_color.clone();
            int scaling = path.getNumPoints() * 10;
            for (int i = 0; i < scaling - 1; i++) {

                path.calculatePoint(v1, i / (float) scaling);
                path.calculatePoint(v2, (i + 1) / (float) scaling);
                backboneSegment.genBackboneSegment(
                        30,
                        0.28f,
                        v1,
                        v2,
                        color
                );
                color[2] += 1/(float) scaling;
            }
        }
        BufferManager.transferToGl();
    }
    */


    /**
     * Adjust the rendering quality if possible to keep the TRI-count
     * within the available memory.   If this is not possible, then
     * return false.
     *
     */
    private fun calcMemoryUsage(dmode: Int): Boolean {

        /*
         * calculate usage with normal slices
         */
        /* geometrySlices = INITIAL_SLICES
         var sphereGeometrySlices = INITIAL_SLICES / 2
         var ribbons: Long = 0
         var sphere: Long = 0
         var bonds: Long = 0

         if (dmode and D_RIBBONS != 0) {
             ribbons = bondAllocation(INITIAL_SLICES).toLong()
         }

         if (dmode and D_BONDS != 0) {
             bonds = ribbonAllocation(INITIAL_SLICES).toLong()
         }

         if (dmode and D_SPHERES != 0) {
             sphere = sphereAllocation(sphereGeometrySlices).toLong()
         }

         val activityManager2 = activity.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
         val info2 = ActivityManager.MemoryInfo()
         activityManager2.getMemoryInfo(info2)
         var initialAvailMem = info2.threshold

         initialAvailMem /= 2  // seems like we only get 1/2 to play with

         if (ribbons + bonds + sphere > initialAvailMem) {
             geometrySlices /= 2

             if (dmode and D_RIBBONS != 0) {
                 ribbons = bondAllocation(geometrySlices).toLong()
             }
             if (dmode and D_BONDS != 0) {
                 bonds = ribbonAllocation(geometrySlices).toLong()
             }
             if (dmode and D_SPHERES != 0) {
                 sphere = sphereAllocation(geometrySlices).toLong()
             }

             if (ribbons + bonds + sphere > initialAvailMem) {
                 val overdraw = initialAvailMem - ribbons - bonds - sphere
                 Timber.e("***  mema  TROUBLE delta: %d r: %d b: %d s: %d avail: %d",
                         overdraw,
                         ribbons,
                         bonds,
                         sphere,
                         initialAvailMem)

                 return false
             }
         } */
        return true  // rendering will fit, hopefully
    }

    /*
     * calculate projected triangle allocations in bytes
     *   based on the "slices" or number of facets in the geometry
     */
    fun bondAllocation(numSlices: Int): Int {
        return mol.bondList.size *
                2 * 6 * (numSlices + 1) * STRIDE_IN_BYTES
    }

    fun sphereAllocation(numSlices: Int): Int {
        return (mol.atomNumberList.size *
                numSlices * numSlices // number of vertices

                * 3 * 2 // two triangles worth generated per loop

                * STRIDE_IN_BYTES) // num floats per vertex
    }

    /*
     * note that there are 10 "cylinders" between nodes in the ribbons
     *    this is a wired-in number so far.
     *       This calculation overestimates the needed space by a little bit.
     */
    fun ribbonAllocation(numSlices: Int): Int {
        return mol.ribbonNodeCount * 10 *
                6 * (numSlices + 1) * STRIDE_IN_BYTES
    }


    companion object {
        /*
         * The Modes
         */
        const val VIEW_INITIAL = 0
        const val VIEW_RIBBONS = 1

        //        const val VIEW_RIBBONS_DEV_ALL = 2
        const val VIEW_RIBBONS_DEV_ALL = 5
        const val VIEW_BALL_AND_STICK = 3
        const val VIEW_STICK = 4

        //        const val VIEW_SPHERE = 5
        const val VIEW_SPHERE = 2
        const val VIEW_CA_QUICK_LINE = 6

        private const val VIEW_TOTAL_MODES = 6

        //    private static final int VIEW_RIBBONS_ONLY = 99;
        //    private static final int VIEW_RIBBONS_AND_BACKBONE = 98;
        //    private static final int VIEW_HELIX_TEST = 97; // skip for now
        //    private static final int VIEW_RIBBONS_PLUS_BALL_AND_STICK = 96;


        private const val D_REAL_RADIUS = 1 shl 1
        private const val D_BALL_RADIUS = 1 shl 2
        private const val D_PIPE_RADIUS = 1 shl 3
        private const val D_NUCLEIC = 1 shl 4
        private const val D_HETATM = 1 shl 5
        private const val D_ALL_ATOMS = 1 shl 6
        private const val D_RIBBONS = 1 shl 7
        private const val D_BONDS = 1 shl 8
        private const val D_SPHERES = 1 shl 9

        private const val INITIAL_SLICES = 20
        private const val SPHERE_SLICES = INITIAL_SLICES / 2 // TODO: make this dynamic
        private const val BYTES_PER_FLOAT = 4
        private const val BYTES_PER_SHORT = 2

        private const val POSITION_DATA_SIZE_IN_ELEMENTS = 3
        private const val NORMAL_DATA_SIZE_IN_ELEMENTS = 3
        private const val COLOR_DATA_SIZE_IN_ELEMENTS = 4

        private const val STRIDE_IN_FLOATS = POSITION_DATA_SIZE_IN_ELEMENTS + NORMAL_DATA_SIZE_IN_ELEMENTS + COLOR_DATA_SIZE_IN_ELEMENTS
        private const val STRIDE_IN_BYTES = STRIDE_IN_FLOATS * BYTES_PER_FLOAT
    }


}
