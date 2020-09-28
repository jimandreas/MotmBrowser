//@file:Suppress("unused", "FunctionName", "IllegalIdentifier")
@file:Suppress("unused")
package com.bammellab.sandbox.display_pdb_file

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.bammellab.mollib.GLSurfaceViewDisplayPdbFile
import com.bammellab.mollib.RendererDisplayPdbFile
import com.google.android.material.snackbar.Snackbar
import com.bammellab.sandbox.R
import timber.log.Timber

//@Suppress("UsePropertyAccessSyntax", "UNUSED_VARIABLE", "unused", "UNUSED_PARAMETER", "DEPRECATION")
//@SuppressWarnings("UnusedParameters", "unused") // Parameters inspected reflectively.

class ActivityDisplayPdbFile : AppCompatActivity(), Handler.Callback {
    /** Hold a reference to our GLSurfaceView  */
    private var mGLSurfaceView: GLSurfaceViewDisplayPdbFile? = null
    private var mRenderer: RendererDisplayPdbFile? = null
    private var mNextViewProgress: ProgressBar? = null

    private var mNextNameIndex = -1
    // wire in the names and display names
    private val pdbFileNames2 = arrayOf("1a0h", "1AGU_simple", // 1 alpha, 3 beta sheets, no Nucleic
            "5ccw_with_gold_ligands", "2n0l_dna_with_hydros", "1bn1", "1bna", "1mfk_rna", "1mat", "2n3v",
            //            "3zeu", // big - throws OOM

            "1x1r")

    // no PDB file (mol too big)
    // "4v6t","4v8q","4v60",
    /*
    "4v44",
            "4v4j",
            "4v4r",
            "4v5d",
            "4v5f",
            "4v5g",
     */

    private var pdbFileDisplayNames2 = arrayOf(
            "1b41",
            "1bna",
            "1a0h", "1AGU alpha and beta", "5CCW with Gold Ligands", "2n0l dna with hydros", "1bn1 single DC", "1bna DNA double helix", "1mfk RNA w hyd", "1mat cobalt links", "2nv3 more hydros",
            //            "3ZEU",

            "1X1R many sheets and helixes")

    // BIG   "1aon",
    //    "1dgi", // lots of red lines

    private val pdbFileNames = arrayOf(

            // spike proteins MOTM 246
            "6crz",
            "6vxx",
            "6m17",
            "6vsb",
            "3bgf",
            "2ghw",
            "6w41",


            "1b41", // curious mol with a eccentric atom
            "1bna",

            "1a0h",

            // "1ana", // short read at 233 HETATM - why? FIXED

            // "4cox",  // https://pdb101.rcsb.org/motm/17 - motm

            "1a0i", "1a1t", "1a31", "1a36", "1a3w", "1a52", "1a59", "1acc", "1acj", "1adc", "1aew", "1agn", "1ajk", "1ajo", "1ak4", "1akj",
            // "1alm",  no structure info - ick
            "1am1", "1am2", "1ana", "1aoi", "1aon", "1aqu", "1asz", "1atn", "1atp", "1au1", "1ax8", "1b41", "1b5s", "1b7t", "1b89", "1b98", "1bbl", "1bbt", "1bd2", "1bdg", "1bet", "1bg2", "1bgl", "1bgw", "1bkd", "1bkv", "1bl8", "1blb", "1bln", "1bna", "1bo4", "1boy", "1bpo", "1br0", "1br1", "1brl", "1buw", "1bx6", "1bzy", "1c17", "1c1e", "1c3w", "1c4e", "1c7d", "1c8m", "1c96", "1c9f", "1ca2", "1cag", "1cam", "1cd3", "1cdw", "1cet", "1cfd", "1cfj", "1cgp", "1cjb", "1cjw", "1ckm", "1ckn", "1cko", "1cll", "1clq", "1cm1", "1cnw", "1cos", "1cpm", "1cq1", "1cqi", "1cts", "1cul", "1cvj", "1cvn", "1cx8", "1cyo", "1d0r", "1d2n", "1d2s", "1d6n", "1dan", "1dar", "1db1", "1dcp", "1ddz", "1dfg", "1dfj", "1dfn", "1dgi", "1dgk", "1dgs", "1dhr", "1dkf", "1dkg", "1dkz", "1dlh", "1dls", "1dmw", "1dog", "1dsy", "1dze", "1e08", "1e0u", "1e12", "1e2o", "1e4e", "1e58", "1e6e", "1e6j", "1e79", "1e7i", "1e9y", "1ea1", "1eaa", "1ebd", "1ee5", "1efa", "1eft", "1efu", "1egf", "1ei1", "1ei7", "1eiy", "1ek9", "1ema", "1eot", "1eqj", "1eri", "1eul", "1euq", "1eve", "1eww", "1exr", "1ezg", "1ezx", "1f1j", "1f5a", "1f6g", "1f88", "1f9j", "1fa0", "1fa3", "1far", "1fbb", "1fdl", "1feh", "1ffk", "1ffx", "1ffy", "1fg9", "1fha", "1fiq", "1fjg", "1fka", "1fkn", "1fnt", "1fo4", "1fok", "1fps", "1fpv", "1fpy", "1fqv", "1fqy", "1fsd", "1fuo", "1fvi", "1fvm", "1fx8", "1fxk", "1fxt", "1fyt", "1fzc", "1g28", "1g3i", "1g7k", "1g8h", "1gax", "1gc1", "1gcn", "1gco", "1gfl", "1gg2", "1gh6", "1gia", "1gnj", "1got", "1gp1", "1gpa", "1gpe", "1gt0", "1gtp", "1gtq", "1gtr", "1gw5", "1gxp", "1gyu", "1h02", "1h0r", "1h2c", "1h68", "1h76", "1hbm", "1hcq", "1he8", "1hfe", "1hge", "1hgu", "1hhg", "1hhh", "1hhi", "1hhj", "1hhk", "1hho", "1hkg", "1hlu", "1hmp", "1hnw", "1hny", "1hox", "1hrn", "1hsa", "1hsg", "1htb", "1htm", "1huy", "1hvb", "1hwg", "1hwh", "1hwk", "1hxb", "1hxw", "1hy1", "1hzh", "1i01", "1i6h", "1i7x", "1i9a", "1ibk", "1ibl", "1ibm", "1ibn", "1ice", "1idc", "1ide", "1idr", "1ig8", "1igt", "1igy", "1ik9", "1ika", "1iod", "1iph", "1ir3", "1irk", "1itf", "1iw7", "1iwg", "1iwo", "1iyt", "1j3h", "1j4e", "1j59", "1j5e", "1j78", "1j8u", "1jb0", "1jey", "1jff", "1jfi", "1jgj", "1jku", "1jky", "1jl4", "1jlb", "1jlu", "1jmb", "1jnu", "1joc", "1jrj", "1jrp", "1jsp", "1jv2", "1jva", "1jz7", "1jz8", "1k4c", "1k4r", "1k4t", "1k5j", "1k83", "1k88", "1k8t", "1k90", "1k93", "1k9o", "1kac", "1kas", "1kbh", "1kdf", "1kdx", "1kgs", "1kil", "1kln", "1kny", "1krv", "1ky7", "1kyo", "1kys", "1l0i", "1l2p", "1l2y", "1l35", "1l3w", "1l8c", "1l8t", "1l9k", "1lac", "1lb2", "1ldk", "1lfg", "1lnq", "1lph", "1ltb", "1lth", "1ltt", "1lu1", "1lws", "1lyb", "1lyd", "1lyz", "1lzi", "1m1j", "1m4h", "1m7g", "1m8o", "1mbn", "1mbo", "1mdt", "1mel", "1mht", "1miu", "1mkx", "1mlc", "1mld", "1mlw", "1mme", "1mol", "1mro", "1msd", "1msw", "1muh", "1mus", "1mwp", "1n0u", "1n0v", "1n0w", "1n20", "1n25", "1n2c", "1n4e", "1n6g", "1n9d", "1nca", "1nci", "1nek", "1nji", "1nkp", "1nn2", "1nod", "1noo", "1nqo", "1nsf", "1nu8", "1nw9", "1nxg", "1o4x", "1o9j", "1oco", "1oei", "1ohf", "1ohg", "1ohr", "1ojx", "1ok8", "1olg", "1om4", "1opl", "1owt", "1oy8", "1oyd", "1oz4", "1p0n", "1p58", "1p8j", "1pah", "1pau", "1phz", "1pma", "1pmb", "1pmr", "1ppb", "1ppi", "1prc", "1prh", "1prt", "1psi", "1psv", "1pth", "1ptu", "1py1", "1pzn", "1q0d", "1q5a", "1q5c", "1qf6", "1qfu", "1qgk", "1qiu", "1qku", "1qle", "1qm2", "1qml", "1qnv", "1qqw", "1qu1", "1qys", "1r4i", "1r4n", "1r4u", "1r5p", "1r7r", "1r8j", "1r9f", "1rcx", "1rd8", "1rfb", "1rh4", "1ri1", "1rlc", "1ron", "1rtn", "1ruz", "1rva", "1rvc", "1rvf", "1rw6", "1rwt", "1rys", "1s58", "1s5l", "1sbt", "1sep", "1set", "1sfc", "1sg1", "1sgz", "1si3", "1skh", "1sl2", "1smd", "1sos", "1st0", "1su4", "1sva", "1svm", "1szp", "1t24", "1t25", "1t2k", "1t8u", "1tau", "1tbd", "1tbg", "1tcf", "1tcr", "1tez", "1tf6", "1tgh", "1tgs", "1tha", "1thj", "1thv", "1tki", "1tlf", "1tll", "1trz", "1ttd", "1ttt", "1tub", "1tui", "1tup", "1tzo", "1u04", "1u19", "1u1z", "1u6b", "1u8d", "1ubq", "1ui9", "1ul1", "1um2", "1ump", "1un6", "1uv6", "1uwh", "1v0d", "1vas", "1vf5", "1vol", "1vpe", "1vpr", "1vsz", "1w0e", "1w36", "1w3b", "1w63", "1w6k", "1w85", "1wa5", "1wb1", "1wfb", "1wio", "1wq1", "1www", "1x70", "1xf0", "1xi4", "1xka", "1xkk", "1xmb", "1xnj", "1xp0", "1xtc", "1y0j", "1y26", "1y27", "1ya5", "1ycq", "1yet", "1yfg", "1ygp", "1yhu", "1yi5", "1ykf", "1ylv", "1ymg", "1ynw", "1ytb", "1ytf", "1yvn", "1yyf", "1z1g", "1z2b", "1z6t", "1z7g", "1zaa", "1zcd", "1zen", "1zes", "1znf", "1zqa", "2a1s", "2a3d", "2a45", "2a7u", "2aai", "2am9", "2amb", "2ayh", "2b3y", "2b4n", "2bat", "2bbm", "2beg", "2bg9", "2biw", "2bku", "2bpt", "2brz", "2btv", "2buk", "2c2a", "2c37", "2cag", "2cas", "2cbj", "2cf2", "2cg9", "2cha", "2ckb", "2cpk", "2crd", "2cts", "2d04", "2d1s", "2d1t", "2dcg", "2dez", "2dhb", "2dln", "2drd", "2e4z", "2ebt", "2eq7", "2ez6", "2ezo", "2f1m", "2f8s", "2fae", "2ffl", "2fk6", "2fp4", "2frv", "2fug", "2g30", "2gbl", "2gfp", "2gls", "2gs6", "2gtl", "2h5o", "2h5q", "2h7d", "2h9r", "2hbs", "2hck", "2hco", "2hft", "2hgh", "2hgt", "2hhb", "2hiu", "2hla", "2hmi", "2hu4", "2i8b", "2ic8", "2ioq", "2ipy", "2irv", "2j0d", "2j1u", "2j67", "2j7w", "2jgd", "2jho", "2jlb", "2jzq", "2k6o", "2k9j", "2ka6", "2kin", "2kj3", "2kod", "2kz1", "2l63", "2l7u", "2lhb", "2lm3", "2lmn", "2lmp", "2lyz", "2m4j", "2mfr", "2mys", "2ncd", "2nll", "2nn6", "2nrf", "2nse", "2nwx", "2o0c", "2o1u", "2o1v", "2o39", "2o60", "2o61", "2o6g", "2oar", "2oau", "2obs", "2om3", "2one", "2onj", "2p04", "2p1h", "2p1p", "2p1q", "2pah", "2pd7", "2pel", "2pf2", "2pgi", "2pi0", "2plv", "2pne", "2ptc", "2ptn", "2q57", "2q66", "2qbz", "2qkm", "2qrv", "2qw7", "2r4r", "2r6p", "2rh1", "2rik", "2rnm", "2sod", "2src", "2taa", "2tbv", "2tmv", "2toh", "2tra", "2uvb", "2uvc", "2uwm", "2v01", "2vaa", "2vab", "2vbc", "2vdo", "2vir", "2vv5", "2wdg", "2wj7", "2xow", "2y0g", "2yhx", "2ymk", "2ypi", "2yye", "2z6c", "2z75", "2z7x", "2zta", "2zxe", "2zyv", "309d", "3a3y", "3ado", "3aic", "3aie", "3b43", "3b4r", "3b75", "3b7e", "3b8c", "3b8e", "3bc8", "3bes", "3biy", "3blw", "3bn4", "3by8", "3c6g", "3cap", "3ciy", "3cl0", "3cln", "3cmp", "3cmx", "3cna", "3cpa", "3cpp", "3csy", "3cyt", "3dag", "3dfr", "3dge", "3dkt", "3e7t", "3ert", "3est", "3et6", "3ets", "3eub", "3eyc", "3f3e", "3f47", "3fcs", "3fke", "3frh", "3fsn", "3fw4", "3fxi", "3g60", "3g61", "3gbi", "3gpd", "3gwv", "3h1j", "3h47", "3hfl", "3hfm", "3hhr", "3hls", "3hvt", "3hz3", "3i0g", "3icd", "3iol", "3irw", "3iwn", "3ixz", "3iyn", "3iyq", "3iyr", "3iz4", "3iz8", "3j1t", "3j2t", "3j2u", "3j3q", "3j3y", "3j5m", "3jad", "3kin", "3kll", "3kud", "3kyh", "3l1e", "3l5q", "3lcb", "3ldh", "3loh", "3lpw", "3lqq", "3m24", "3m7r", "3m9s", "3mge", "3mon", "3n0g", "3nhc", "3og7", "3os0", "3os1", "3p05", "3p5p", "3pb3", "3pe4", "3pgk", "3pgm", "3pqr", "3psg", "3pt6", "3pte", "3q6x", "3r1k", "3r8f", "3rh8", "3rif", "3rko", "3rxw", "3s0x", "3sdp", "3sdy", "3se7", "3sfz", "3sm5", "3srp", "3tnp", "3tom", "3tt1", "3tt3", "3twy", "3u5d", "3u5e", "3u5z", "3ugm", "3unf", "3ux4", "3v6o", "3v6t", "3vcd", "3vdx", "3vie", "3vkh", "3vne", "3w14", "3zoj", "3zpk", "4ajx", "4ald", "4ape", "4ar7", "4blm", "4cc8", "4cms", "4cox", "4CR2", "4dpv", "4eeu", "4egg", "4ers", "4esv", "4eyl", "4fqi", "4gbc", "4gcr", "4gcz", "4hfe", "4hhd", "4iar", "4ib4", "4icd", "4imv", "4ins", "4iyf", "4j7u", "4jhw", "4kf5", "4l6r", "4ldb", "4ldd", "4lp5", "4m48", "4m4w", "4mmv", "4mne", "4nco", "4oaa", "4oo8", "4or2", "4p6i", "4pfk", "4pti", "4q21", "4qb0", "4qqw", "4qyz", "4rhv", "4rxn", "4tna", "4tnv", "4tvx", "4u7u", "4un3",


            "4xia", // too big previous pdbs removed
            "4xv1", "4xv2", "4zhd", "5a1a", "5ire", "5mdh", "5p21", "5pep", "5rsa", "6adh", "6gpb", "6ldh", "6pfk", "6tna", "7acn", "7dfr", "7hvp", "8cat", "8gpb", "8icd", "8ruc", "9icd", "9pap", "9rub")

    private fun loadNextPdbFile() {

        if (++mNextNameIndex == pdbFileNames.size) {
            mNextNameIndex = 0
        }
        val name = pdbFileNames[mNextNameIndex]
        Timber.d("Next file: %s", name)
        //        setTitle(pdb_file_display_name[mNextNameIndex]);
        title = pdbFileNames[mNextNameIndex]
        mRenderer!!.setPdbFileName(name)

        mGLSurfaceView!!.queueEvent { mRenderer!!.loadPdbFile() }
    }

    private fun loadPrevPdbFile() {

        if (mNextNameIndex-- == 0) {
            mNextNameIndex = pdbFileNames.size - 1
        }
        val name = pdbFileNames[mNextNameIndex]
        Timber.d("Next file: %s", name)
        //        setTitle(pdb_file_display_name[mNextNameIndex]);
        title = pdbFileNames[mNextNameIndex]
        mRenderer!!.setPdbFileName(name)

        mGLSurfaceView!!.queueEvent { mRenderer!!.loadPdbFile() }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_pdb_file)

        val handler = Handler(
                Looper.getMainLooper(), this)
        MainContext.INSTANCE.handler = handler

        mGLSurfaceView = findViewById(R.id.gl_surface_view)
        mNextViewProgress = findViewById(R.id.next_view_progress_circle)

        // Check if the system supports OpenGL ES 2.0.
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000

        if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.
            mGLSurfaceView!!.setEGLContextClientVersion(2)

            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)

            mRenderer = RendererDisplayPdbFile(this, mGLSurfaceView!!, handler)
            mGLSurfaceView!!.setRenderer(mRenderer!!, displayMetrics.density)
            // This freezes the updates, now adjusted in GLSurfaceView
            // mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        } else {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            return
        }

        loadNextPdbFile()

        findViewById<View>(R.id.button_next_obj).setOnClickListener { loadNextPdbFile() }

        findViewById<View>(R.id.button_prev_obj).setOnClickListener { loadPrevPdbFile() }

        //		findViewById(R.id.button_switch_rendering_mode).setOnClickListener(new View.OnClickListener() {
        //			@Override
        //			public void onClick(View v) {
        //				toggleWireframe();
        //			}
        //		});

        findViewById<View>(R.id.button_select).setOnClickListener { toggleSelect() }

        findViewById<View>(R.id.button_change_viewmode).setOnClickListener {
            mNextViewProgress!!.visibility = View.VISIBLE
            mGLSurfaceView!!.queueEvent { mRenderer!!.nextViewMode() }
        }
    }


    override fun onResume() {
        // The activity must call the GL surface view's onResume() on activity
        // onResume().
        super.onResume()
        mGLSurfaceView!!.onResume()
    }

    override fun onPause() {
        // The activity must call the GL surface view's onPause() on activity
        // onPause().
        super.onPause()
        mGLSurfaceView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mRenderer!!.doCleanUp()
    }

    private fun toggleShader() {
        mGLSurfaceView!!.queueEvent { mRenderer!!.toggleShader() }
    }

    private fun toggleHydrogenDisplayMode() {
        mGLSurfaceView!!.queueEvent { mRenderer!!.toggleHydrogenDisplayMode() }
    }

    private fun toggleWireframe() {
        mGLSurfaceView!!.queueEvent { mRenderer!!.toggleWireframeFlag() }
    }

    private fun toggleSelect() {
        mGLSurfaceView!!.queueEvent { mRenderer!!.toggleSelectFlag() }
    }

    fun changeViewIsFinished() {
        runOnUiThread { mNextViewProgress!!.visibility = View.INVISIBLE }
    }

    fun noMemoryForAtomView() {
        runOnUiThread {
            Snackbar.make(findViewById(R.id.frame_layout),
                    "Not Enough Mem for Atom View", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    // no button anymore for shader
    fun updateShaderStatus() {
        //        runOnUiThread(new Runnable() {
        //            @Override
        //            public void run() {
        //                if (useVertexShading) {
        //                    ((Button) findViewById(R.id.button_switch_shaders)).setText(R.string.button_objects_using_pixel_shading);
        //                } else {
        //                    ((Button) findViewById(R.id.button_switch_shaders)).setText(R.string.button_objects_using_vertex_shading);
        //                }
        //            }
        //        });
    }
    //    public void updateWireframeStatus(final boolean wireFrameRendering) {
    //        runOnUiThread(new Runnable() {
    //            @Override
    //            public void run() {
    //                if (wireFrameRendering) {
    //                    ((Button) findViewById(
    //                            R.id.button_switch_rendering_mode)).setText(R.string.button_objects_using_triangle_rendering);
    //                } else {
    //                    ((Button) findViewById(
    //                            R.id.button_switch_rendering_mode)).setText(R.string.button_objects_using_wireframe_rendering);
    //                }
    //            }
    //        });
    //    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_wireframe) {
            //            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            //            startActivity(intent);
            // Timber.w("wireframe");
            toggleWireframe()
            return true
        }
        //        if (id == R.id.action_select ) {
        //            toggleSelect(); // toggle for now
        //            return true;
        //        }

        if (id == R.id.action_shading) {
            toggleShader() // toggle for now
            return true
        }

        if (id == R.id.action_hydrogens) {
            toggleHydrogenDisplayMode() // toggle for now
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * receive messages from the renderer
     * Currently:
     * GL_READY - the GL components are created - instance state can
     * now be set (like for device rotation)
     * UPDATE_RPM - RPM indicator - to be implemented
     * @param msg  message sent from renderer
     * @return true - TODO: understand if this means something
     */

    override fun handleMessage(msg: Message): Boolean {

        // http://stackoverflow.com/a/27659565/3853712


//        @MainContext.UImessages val what = msg.what
//
//        when (what) {
//            UI_MESSAGE_GL_READY -> {
//            }}

        //                renderer.updateColor(MyGLRenderer.RENDER_SET_COLOR_R, sliderRed.getProgress());
        //                renderer.updateColor(MyGLRenderer.RENDER_SET_COLOR_G, sliderGreen.getProgress());
        //                renderer.updateColor(MyGLRenderer.RENDER_SET_COLOR_B, sliderBlue.getProgress());
        //                int rpm = msg.arg1;
        //                if (rpm < 0) {
        //                    rpm *= -1;
        //                }
        //                if (speedometer != null) {
        //                    speedometer.setSpeed((double) rpm);
        //                }
        return true
    }

    companion object {
//        private val LOG_TAG = "activity"
    }
}