/*
 * Copyright (C) 2016-2018 James Andreas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

@file:Suppress("unused")
package com.bammellab.mollib.objects


import android.app.Activity
import android.app.ActivityManager
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import com.bammellab.mollib.protein.AtomInfo
import com.bammellab.mollib.protein.Molecule
import com.bammellab.mollib.protein.PdbAtom
import timber.log.Timber

// TODO: implement Interface in Activity - for flagging low mem in UI (commented out after lib refactor)
class ManagerViewmode(private val mActivity: Activity,
                      private val mMol: Molecule,
                      private val mBufMgr: BufferManager
                      ) {

    private val mAtomSphere: AtomSphere
    private val mAtomToAtomBond: SegmentAtomToAtomBond
    private val mRenderModal: RenderModal
    private val mRenderNucleic: RenderNucleic
    private val mAtomInfo: ParserAtomInfo
    private var mCurrentMode: Int = 0

    init {
        mMol.mBufMgr = mBufMgr
        mAtomSphere = AtomSphere(mMol)
        mAtomToAtomBond = SegmentAtomToAtomBond(mMol)
        mRenderModal = RenderModal(mMol)
        mRenderNucleic = RenderNucleic(mMol)
        mAtomInfo = ParserAtomInfo(mActivity)
        mAtomInfo.parseAtomInfo()
    }

    fun createView() {
        mCurrentMode = VIEW_INITIAL
        nextViewMode()
    }

    //    public void repeatViewMode() {
    //        doViewMode();
    //    }
    fun nextViewMode() {

        if (++mCurrentMode > VIEW_TOTAL_MODES) {
            mCurrentMode = VIEW_INITIAL + 1
        }

        doViewMode()

        /*
         * let the UI know that the mode change is completed (to flush the spinner)
         */
//        val message = Message.obtain(handler, Molecule.UI_MESSAGE_FINISHED_VIEW_CHANGE)
//        handler.dispatchMessage(message)
    }

    /*

calculated:

39 456 000 bonds: 7 094 400

105 120 000 bonds: 21 148 800  2nv3 more hydros

     tablet
availMem = 152 883 200 (0x91CD000)
foregroundAppThreshold = 19 796 992 (0x12E1400)
hiddenAppThreshold = 46 472 192 (0x2C51C00)
visibleAppThreshold = 25 891 840 (0x18B1400)
secondaryServerThreshold = 46472192 (0x2C51C00)
threshold = 46472192 (0x2C51C00)
totalMem = 839122944 (0x32040000)
lowMemory = false

   nexus phone
availMem = 1 305 042 944 (0x4DC96000)
foregroundAppThreshold = 75 497 472 (0x4800000)
hiddenAppThreshold = 150 994 944 (0x9000000)
lowMemory = false
secondaryServerThreshold = 150994944 (0x9000000)
threshold = 150994944 (0x9000000)
totalMem = 1924923392 (0x72BC0000)
visibleAppThreshold = 94371840 (0x5A00000)
     */

    private fun doViewMode() {

        val startTime = SystemClock.uptimeMillis().toFloat()
        mMol.mReportedTimeFlag = false
        mMol.mStartOfParseTime = startTime

        mBufMgr.resetBuffersForNextUsage()

        /*
         * let's see how much memory there is to play with
         */


        val activityManager2 = mActivity.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        val mInfo2 = ActivityManager.MemoryInfo()
        activityManager2.getMemoryInfo(mInfo2)
        val initialAvailMem = mInfo2.threshold
        try {
            run bailout@ {
                Timber.i("THRESHOLD mbyte = " + initialAvailMem / 1024 / 1024)

                when (mCurrentMode) {
                    VIEW_RIBBONS -> {
                        sDrawMode = D_PIPE_RADIUS or D_NUCLEIC or D_HETATM or D_RIBBONS
                        calcMemoryUsage(sDrawMode)
                        mRenderModal.renderModal()
                        mRenderNucleic.renderNucleic()
                        drawNucleicBonds()
                        drawHetatmBonds()
                        drawSpheres()
                    }

                    VIEW_RIBBONS_DEV_ALL -> {
                        sDrawMode = (D_BALL_RADIUS or D_NUCLEIC or D_HETATM or D_ALL_ATOMS
                                or D_RIBBONS or D_BONDS or D_SPHERES)
                        /*
                 * if there isn't enough memory, then drop back into ribbon mode
                 *    for now.   Save the check for no ribbons for later
                 *    TODO:  handle the no ribbon case
                 */
                        if (!calcMemoryUsage(sDrawMode)) {
                            sDrawMode = D_PIPE_RADIUS or D_NUCLEIC or D_HETATM or D_RIBBONS
                            mCurrentMode = VIEW_RIBBONS
                            mRenderModal.renderModal()
                            mRenderNucleic.renderNucleic()
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
                        mRenderModal.renderModal()
                        mRenderNucleic.renderNucleic()
                    }

                    VIEW_STICK -> {
                        sDrawMode = (D_PIPE_RADIUS or D_NUCLEIC or D_HETATM or D_ALL_ATOMS
                                or D_BONDS or D_SPHERES)
                        calcMemoryUsage(sDrawMode)
                        drawPipeModel()
                        drawSpheres()
                    }
                    VIEW_BALL_AND_STICK -> {
                        sDrawMode = (D_BALL_RADIUS or D_NUCLEIC or D_HETATM or D_ALL_ATOMS
                                or D_BONDS or D_SPHERES)
                        calcMemoryUsage(sDrawMode)
                        drawPipeModel()
                        drawSpheres()
                    }

                    VIEW_SPHERE -> {
                        sDrawMode = (D_REAL_RADIUS or D_NUCLEIC or D_HETATM or D_ALL_ATOMS
                                or D_SPHERES)
                        calcMemoryUsage(sDrawMode)
                        drawSpheres()
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Crashed displaying: %s", mMol.name)
        }

    }

    /*
             * obsolete now - but
             *    it was fun while it lasted
             */
    //            case VIEW_RIBBONS_ONLY:
    //                mRenderAlphaHelix.renderAlphaHelices();
    //                mRenderBetaRibbon.renderBetaRibbons();
    //                mRenderRibbon.renderRibbon();
    //                // renderBackbone();
    //                break;
    //            case VIEW_RIBBONS_AND_BACKBONE:
    //                mRenderAlphaHelix.renderAlphaHelices();
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
         * old diags follow:
         */

    //        Log.v(LOG_TAG,"*** mema " + mInfo.availMem/1024/1024
    //            + " getMemoryClass() = " + activityManager.getMemoryClass());
    //
    //        Timber.i("finished visualization in" + pretty_print + " seconds. " + start_time + " " + end_time);


    /*
     * Draw: pipe model
     */
    private fun drawPipeModel() {

        val color = floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f)

        var atom1Number: Int
        var atom2Number: Int
        var atom1: PdbAtom?
        var atom2: PdbAtom?

        for (i in 0 until mMol.bondList.size) {
            atom1Number = mMol.bondList[i].atomNumber1
            atom2Number = mMol.bondList[i].atomNumber2

            atom1 = mMol.atoms[atom1Number]
            atom2 = mMol.atoms[atom2Number]
            if (atom1 == null || atom2 == null) {
                Timber.e("null ptr : atom1: $atom1 atom2: $atom2")
                continue
            }

            mAtomToAtomBond.genBondCylinders(
                    mMol.geometrySlices,
                    0.25f,
                    atom1,
                    atom2,
                    color,
                    mAtomInfo)

        }
    }

    /*
     * Draw: pipe bond model for nucleic atoms
     */
    private fun drawNucleicBonds() {

        val color = floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f)

        var atom1Number: Int
        var atom2Number: Int
        var atom1: PdbAtom?
        var atom2: PdbAtom?

        for (i in 0 until mMol.bondList.size) {
            atom1Number = mMol.bondList[i].atomNumber1
            atom2Number = mMol.bondList[i].atomNumber2

            atom1 = mMol.atoms[atom1Number]
            atom2 = mMol.atoms[atom2Number]

            if (atom1 == null || atom2 == null) {
                Timber.e("null ptr : atom1: $atom1 atom2: $atom2")
                continue
            }
            // if (!atom1.is_nucleic_atom || !atom2.is_nucleic_atom) {
            if (atom1.atomType != PdbAtom.IS_NUCLEIC || atom2.atomType != PdbAtom.IS_NUCLEIC) {

                continue
            }

            if (!mMol.displayHydrosFlag) {
                if (atom1.elementSymbol == "H" || atom2.elementSymbol == "H") {

                    continue
                }
            }
            mAtomToAtomBond.genBondCylinders(
                    mMol.geometrySlices,
                    0.25f,
                    atom1,
                    atom2,
                    color,
                    mAtomInfo)

        }
    }

    /*
     * Draw: pipe bond model for HETATMs
     *     changes for this method:
     *        if one of the atoms is a metal
     */
    private fun drawHetatmBonds() {

        val color = floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f)

        var atom1Number: Int
        var atom2Number: Int
        var atom1: PdbAtom?
        var atom2: PdbAtom?

        for (i in 0 until mMol.bondList.size) {
            atom1Number = mMol.bondList[i].atomNumber1
            atom2Number = mMol.bondList[i].atomNumber2

            atom1 = mMol.atoms[atom1Number]
            atom2 = mMol.atoms[atom2Number]

            if (atom1 == null || atom2 == null) {
                Timber.e("null ptr : atom1: $atom1 atom2: $atom2")
                continue
            }
            if (atom1.atomType != PdbAtom.IS_HETATM && atom2.atomType != PdbAtom.IS_HETATM) {
                continue
            }

            if (!mMol.displayHydrosFlag) {
                if (atom1.elementSymbol == "H" || atom2.elementSymbol == "H") {

                    continue
                }
            }

            mAtomToAtomBond.genBondCylinders(
                    mMol.geometrySlices,
                    0.25f,
                    atom1,
                    atom2,
                    color,
                    mAtomInfo)

        }
    }

    /**
     * TODO: right now this method is called for both ribbon mode and molecule mode
     *
     */
    private fun drawSpheres() {

        var atom1: PdbAtom?
        var elementSymbol: String
        var ai: AtomInfo?
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
            sDrawMode and D_REAL_RADIUS == D_REAL_RADIUS -> lookupRadius = true
            sDrawMode and D_BALL_RADIUS == D_BALL_RADIUS -> radius = 0.50f
            else -> radius = 0.25f
        }

        for (i in 0 until mMol.atoms.size) {
            atom1 = mMol.atoms[mMol.numList[i]]
            if (atom1 == null) {
                Timber.e("drawSpheres: error - got null for " + mMol.numList[i])
                continue
            }
            // skip HOH (water) molecules
            if (atom1.atomType == PdbAtom.IS_HETATM && atom1.residueName == "HOH") {
                continue
            }
            if (atom1.atomType == PdbAtom.IS_NUCLEIC) {
                if (sDrawMode and D_NUCLEIC == 0) {
                    continue
                }
            } else if (atom1.atomType == PdbAtom.IS_HETATM) {
                if (sDrawMode and D_HETATM == 0) {
                    continue
                }
            } else if (atom1.elementSymbol == "H") {
                if (!mMol.displayHydrosFlag) {
                    continue
                } else if (sDrawMode and D_ALL_ATOMS == 0) {
                    continue
                }
            } else if (sDrawMode and D_ALL_ATOMS == 0) {
                continue
            }
            if (atom1.atomBondCount == 0) {
                Timber.e("%s: drawSpheres no bond at atom %d residue %s type %s",
                        mMol.name, atom1.atomNumber, atom1.residueName, atom1.atomName)
            }
            elementSymbol = atom1.elementSymbol
            ai = mAtomInfo.atomNameToAtomInfoHash[elementSymbol]
            useColor = ai?.color ?: color
            /*
             * for full sized atom mode, look up the radius
             */
            if (lookupRadius) {
                radius = if (ai == null) {
                    Timber.e("drawSpheres: no AtomInfo at atom " + atom1.atomNumber +
                            " residue " + atom1.residueName + " type " + atom1.atomName)
                    .25f
                } else {
                    ai.vdwRadius.toFloat() / 100f
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
            mAtomSphere.genSphere(
                    mMol.sphereGeometrySlices,
                    useRadius,
                    atom1,
                    useColor)
        }
        mBufMgr.transferToGl()
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

        for (int i = 0; i < mMol.mListofChainDescriptorLists.size(); i++) {
            pdb_backbone_list = mMol.mListofChainDescriptorLists.get(i);
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
                mBackboneSegment.genBackboneSegment(
                        30,
                        0.28f,
                        v1,
                        v2,
                        color
                );
                color[2] += 1/(float) scaling;
            }
        }
        mBufMgr.transferToGl();
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

        mMol.geometrySlices = Molecule.INITIAL_SLICES
        mMol.sphereGeometrySlices = Molecule.INITIAL_SLICES / 2
        var ribbons: Long = 0
        var sphere: Long = 0
        var bonds: Long = 0

        if (dmode and D_RIBBONS != 0) {
            ribbons = mMol.bondAllocation(Molecule.INITIAL_SLICES).toLong()
        }

        if (dmode and D_BONDS != 0) {
            bonds = mMol.ribbonAllocation(Molecule.INITIAL_SLICES).toLong()
        }

        if (dmode and D_SPHERES != 0) {
            sphere = mMol.sphereAllocation(mMol.sphereGeometrySlices).toLong()
        }

        val activityManager2 = mActivity.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        val mInfo2 = ActivityManager.MemoryInfo()
        activityManager2.getMemoryInfo(mInfo2)
        var initialAvailMem = mInfo2.threshold

        initialAvailMem /= 2  // seems like we only get 1/2 to play with

        if (ribbons + bonds + sphere > initialAvailMem) {
            mMol.geometrySlices = mMol.geometrySlices / 2

            if (dmode and D_RIBBONS != 0) {
                ribbons = mMol.bondAllocation(mMol.geometrySlices).toLong()
            }
            if (dmode and D_BONDS != 0) {
                bonds = mMol.ribbonAllocation(mMol.geometrySlices).toLong()
            }
            if (dmode and D_SPHERES != 0) {
                sphere = mMol.sphereAllocation(mMol.geometrySlices).toLong()
            }

            if (ribbons + bonds + sphere > initialAvailMem) {
                val overdraw = initialAvailMem - ribbons - bonds - sphere
                Timber.e("***  mema  TROUBLE delta: "
                        + overdraw
                        + " r : " + ribbons
                        + " b: " + bonds
                        + " s: " + sphere
                        + " avail: " + initialAvailMem)

                /*
                 * whack the sphere quality and try again
                 */
                mMol.sphereGeometrySlices = 5
                mMol.ribbonSlices = 5
                sphere = mMol.sphereAllocation(mMol.sphereGeometrySlices).toLong()

                if (ribbons + bonds + sphere > initialAvailMem) {
                    val overdraw2 = initialAvailMem - ribbons - bonds - sphere
                    Timber.e("***  mema  STILL TROUBLE delta: "
                            + overdraw2
                            + " r : " + ribbons
                            + " b: " + bonds
                            + " s: " + sphere
                            + " avail: " + initialAvailMem)
                    mMol.geometrySlices = 3
                    mMol.sphereGeometrySlices = 3
                    mMol.ribbonSlices = 5
                    return false
                } else {
                    val overdraw2 = initialAvailMem - ribbons - bonds - sphere
                    Timber.e("***  mema  OK NO TROUBLE positive delta: "
                            + overdraw2
                            + " r : " + ribbons
                            + " b: " + bonds
                            + " s: " + sphere
                            + " have set sphere to 5")
                }
            }
        }
        return true  // rendering will fit, hopefully
    }

    companion object {
        /*
         * The Modes
         */
        private const val VIEW_INITIAL = 0
        private const val VIEW_RIBBONS = 1
        private const val VIEW_RIBBONS_DEV_ALL = 2
        private const val VIEW_BALL_AND_STICK = 3
        private const val VIEW_STICK = 4
        private const val VIEW_SPHERE = 5

        private const val VIEW_TOTAL_MODES = 5

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

        private var sDrawMode: Int = 0
    }
}
