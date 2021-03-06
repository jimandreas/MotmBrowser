/*
 *  Copyright 2021 Bammellab / James Andreas
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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.bammellab.captureimages

object MotmPdbNames {

    val pdbNames2021 = listOf(
        "1b86",
        "1ckt",
        "1fdh",
        "1glu",
        "1m2z",
        "2h4z",
        "2icy",
        "2r7z",
        "2r8k",
        "3dnb",
        "3lpv",
        "3s27",
        "4hg6",
        "4hhb",
        "4k2c",
        "4p6x",
        "5a39",
        "5djl",
        "5my1",
        "6bs1",
        "6ki6",
        "6m97",
        "6wlb",
        "6xk0",
        "6zdh",
        "6zxn",
        "7c2l",
        "7cwn",
        "7cwu",
        "7k43",
        "7k8t",
        "7kkl",
        "7l06",
            
        )

    val pdbNamesJan18 = listOf(
        "5my1",
        "3fcs",
        "2c37",
        "1su4"
    )

    val pdbNamesDec18 = listOf(
        "4xxb",
        "1bna",
        "4fe5",
        "1zh1",
        //           "3cmx",
//            "2tbv",
//            "1tzo",
//            "1iw7",
//            "1w63",
//            "1hnw",
//            "1nji",
//            "1fjg",
//            "4ox9",
//            "5y9f",
//            "5ijn",
//            "5a9q",
//            "4tnv",
//            "4u7u",
//            "4CR2",
//            "1pma",
//            "1ibk",
//            "1ibl",
//            "1ibm",
//            "1j5e",
//            "6cgv",
//            "3aic",
//            "3aie",
//            "2fug",
//            "1YQV",
//            "3CF1",
//            "5OWU",
//            "2C37",
//            "2VGL",
        //  "1XI4",  // infinite loop???
        // "3SNP",
        "3CMX",
//            "4AC9",
//            "5KSD",
//            "4FE5",
//            "3FCS",
//            "5UGY",
        // "4TVX", ??
        "4ZXB",
        "5KQV",
//            "5ZGE",
//            "2btv",
//            "5GAR",
//            "5voz",
//            "6XQB",
//            "1ZH1",
//            "2OC8",
//            "4SV6",

    )
    val pdbNames: List<String> = pdbNamesJan18

    val pdbNamesOctNovDec2020 = listOf(

        "6dmw",
        "5irz",
        "5irx",

        "5is0",


        "6r3q",
        "4clk",
        "5u6p",
        "1cjk",

        "1cu1",
        "4wtg",
        "1zh1",
        "1r7g",
        "2oc8",
        "3sv6",
        "3sue",
        "6p6l",
        "6nzt"
    )


    val pdbNamesProblems = listOf(
        "3cmx",
        "1fka",
        "1r9f",
        "2z75",

        "4dpv",
        "4qqw",
        "4ts2",
        "6c63",
        "6d6v"

    )
    val pdbNames2 = listOf(
        "2c37",
        // "2tbv",  // next carbon problems
        // "3fcs",  // next carbon problems
        // "3j3q",  // no PDB file only CIF
        // "3j3y", // no PDB file only CIF
        // "3jc8", // no PDB file only CIF
        // "3jc9", // no PDB file only CIF
        // "4tvx", // ditto
        // "4V60",
        // "4v6t",
        // "4v8q",
        "4xxb",
        "5gar",
        "5mnj",
        "5vox",
        "5voy",
        "5xnl", // 99K atoms!
        "5zji",
        // "6by7", // no PDB file only CIF
        "6crz",
        "6kac",
        // "6kad", // no PDB file only CIF
        // "6nwa",
        "6o81",
        "6o9z",

        "1xi4"  // this one is trouble

    )

    /*
     * PDBs with num of Atoms > 50K removed
     */
    val pdbNamesFULL = listOf(

        "143d",
        "148l",
        "173d",
        "1a0h",
        "1a0i",
        "1a1t",
        "1a31",
        "1a36",
        "1a3w",
        "1a52",
        "1a59",
        "1a9w",
        "1acc",
        "1acj",
        "1adc",
        "1aew",
        "1agn",
        "1ajk",
        "1ajo",
        "1ak4",
        "1akj",
        "1am1",
        "1am2",
        "1ana",
        "1aoi",
        "1aon",
        "1ap8",
        "1aqu",
        "1asz",
        "1atn",
        "1atp",
        "1au1",
        "1ax8",
        "1b41",
        "1b5s",
        "1b7t",
        "1b89",
        "1b98",
        "1bbl",
        "1bbt",
        "1bd2",
        "1bdg",
        "1bet",
        "1bg2",
        "1bgl",
        "1bgw",
        "1bi7",
        "1bkd",
        "1bkv",
        "1bl8",
        "1blb",
        "1bln",
        "1bna",
        "1bo4",
        "1boy",
        "1bp2",
        "1bpo",
        "1br0",
        "1br1",
        "1brl",
        "1buw",
        "1bx2",
        "1bx6",
        "1bzy",
        "1c17",
        "1c1e",
        "1c3w",
        "1c4e",
        "1c7d",
        "1c8m",
        "1c96",
        "1c9f",
        "1ca2",
        "1cag",
        "1cam",
        "1cd3",
        "1cdw",
        "1cet",
        "1cfd",
        "1cfj",
        "1cgp",
        "1cjb",
        "1cjw",
        "1cjy",
        "1ckm",
        "1ckn",
        "1cko",
        "1cll",
        "1clq",
        "1cm1",
        "1cnw",
        "1cos",
        "1cpm",
        "1cq1",
        "1cqi",
        "1cts",
        "1cul",
        "1cvj",
        "1cvn",
        "1cx8",
        "1cyo",
        "1d09",
        "1d0r",
        "1d2n",
        "1d2s",
        "1d6n",
        "1dan",
        "1dar",
        "1db1",
        "1dcp",
        "1ddz",
        "1dfg",
        "1dfj",
        "1dfn",
        "1dgi",
        "1dgk",
        "1dgs",
        "1dhr",
        "1dkf",
        "1dkg",
        "1dkq",
        "1dkz",
        "1dlh",
        "1dls",
        "1dmw",
        "1dog",
        "1dsy",
        "1dze",
        "1e08",
        "1e0u",
        "1e12",
        "1e2o",
        "1e4e",
        "1e58",
        "1e6e",
        "1e6j",
        "1e79",
        "1e7i",
        "1e9y",
        "1ea1",
        "1eaa",
        "1ebd",
        "1ee5",
        "1efa",
        "1eft",
        "1efu",
        "1egf",
        "1ei1",
        "1ei7",
        "1eiy",
        "1ej1",
        "1ej4",
        "1ejh",
        "1ek9",
        "1ema",
        "1eot",
        "1eqj",
        "1eri",
        "1eul",
        "1euq",
        "1eve",
        "1eww",
        "1exr",
        "1ezg",
        "1ezx",
        "1f1j",
        "1f5a",
        "1f6g",
        "1f88",
        "1f9j",
        "1fa0",
        "1fa3",
        "1far",
        "1fbb",
        "1fdh",
        "1fdl",
        "1feh",
        "1ffk",
        "1ffx",
        "1ffy",
        "1fg9",
        "1fha",
        "1fin",
        "1fiq",
        "1fka",
        "1fkn",
        "1fnt",
        "1fo4",
        "1fok",
        "1fps",
        "1fpv",
        "1fpy",
        "1fqv",
        "1fqy",
        "1fsd",
        "1fuo",
        "1fvi",
        "1fvm",
        "1fx8",
        "1fxk",
        "1fxt",
        "1fyt",
        "1fzc",
        "1g28",
        "1g3i",
        "1g4q",
        "1g7k",
        "1g8h",
        "1gax",
        "1gc1",
        "1gcn",
        "1gco",
        "1gfl",
        "1gg2",
        "1gh6",
        "1gia",
        "1gnj",
        "1got",
        "1gp1",
        "1gpa",
        "1gpe",
        "1gt0",
        "1gtp",
        "1gtq",
        "1gtr",
        "1gw5",
        "1gxp",
        "1gyu",
        "1h02",
        "1h0r",
        "1h15",
        "1h2c",
        "1h2n",
        "1h68",
        "1h76",
        "1hbm",
        "1hco",
        "1hcq",
        "1he8",
        "1hfe",
        "1hge",
        "1hgu",
        "1hhg",
        "1hhh",
        "1hhi",
        "1hhj",
        "1hhk",
        "1hho",
        "1hkg",
        "1hlu",
        "1hmp",
        "1hny",
        "1hox",
        "1hrn",
        "1hsa",
        "1hsg",
        "1htb",
        "1htm",
        "1huy",
        "1hvb",
        "1hwg",
        "1hwh",
        "1hwk",
        "1hxb",
        "1hxw",
        "1hy1",
        "1hzh",
        "1i01",
        "1i3d",
        "1i6h",
        "1i7x",
        "1i9a",
        "1ibn",
        "1ice",
        "1idc",
        "1ide",
        "1idr",
        "1ig8",
        "1igt",
        "1igy",
        "1ihp",
        "1ik9",
        "1ika",
        "1iod",
        "1iph",
        "1ir3",
        "1irk",
        "1itf",
        "1iwg",
        "1iwo",
        "1iyt",
        "1j3h",
        "1j4e",
        "1j59",
        "1j78",
        "1j8u",
        "1jb0",
        "1jey",
        "1jff",
        "1jfi",
        "1jgj",
        "1jku",
        "1jky",
        "1jl4",
        "1jlb",
        "1jlu",
        "1jmb",
        "1jnu",
        "1joc",
        "1jrj",
        "1jrp",
        "1jsp",
        "1jsu",
        "1jud",
        "1jv2",
        "1jva",
        "1jz7",
        "1jz8",
        "1k4c",
        "1k4r",
        "1k4t",
        "1k5j",
        "1k83",
        "1k88",
        "1k8t",
        "1k90",
        "1k93",
        "1k9o",
        "1kac",
        "1kas",
        "1kbh",
        "1kdf",
        "1kdx",
        "1kgs",
        "1kil",
        "1kln",
        "1kny",
        "1krv",
        "1ky7",
        "1kyo",
        "1kys",
        "1l0i",
        "1l2p",
        "1l2y",
        "1l35",
        "1l3w",
        "1l8c",
        "1l8t",
        "1l9k",
        "1lac",
        "1lb2",
        "1ldk",
        "1lfg",
        "1lhs",
        "1lm8",
        "1lnq",
        "1lph",
        "1lqb",
        "1ltb",
        "1lth",
        "1ltt",
        "1lu1",
        "1lws",
        "1lyb",
        "1lyd",
        "1lyz",
        "1lzi",
        "1m1j",
        "1m4h",
        "1m7g",
        "1m8o",
        "1mbn",
        "1mbo",
        "1mdt",
        "1mel",
        "1mh1",
        "1mht",
        "1miu",
        "1mkx",
        "1mlc",
        "1mld",
        "1mlw",
        "1mme",
        "1mol",
        "1mro",
        "1mrr",
        "1msd",
        "1msw",
        "1muh",
        "1mus",
        "1mwp",
        "1n0u",
        "1n0v",
        "1n0w",
        "1n20",
        "1n25",
        "1n2c",
        "1n4e",
        "1n6g",
        "1n73",
        // "1n9d", // Entry 1N9D was removed from the distribution of released PDB entries (status Obsolete) on 2009-05-19.
        "1nca",
        "1nci",
        "1nek",
        "1nkp",
        "1nn2",
        "1nod",
        "1noo",
        "1nqo",
        "1nsf",
        "1nu8",
        "1nw9",
        "1nxg",
        "1o4x",
        "1o9j",
        "1oco",
        "1oei",
        "1ohf",
        "1ohg",
        "1ohr",
        "1oj6",
        "1ojx",
        "1ok8",
        "1olg",
        "1om4",
        "1opl",
        "1owt",
        "1oy8",
        "1oyd",
        "1oz4",
        "1p0n",
        "1p58",
        "1p8j",
        "1pah",
        "1pau",
        "1phz",
        "1pkq",
        "1plq",
        "1pmb",
        "1pmr",
        "1pob",
        "1poc",
        "1ppb",
        "1ppi",
        "1prc",
        "1prh",
        "1prt",
        "1psh",
        "1psi",
        "1psv",
        "1pth",
        "1ptu",
        "1pv6",
        "1pw4",
        "1py1",
        "1pzn",
        "1q0d",
        "1q2w",
        "1q5a",
        "1q5c",
        "1q8h",
        "1qf6",
        "1qfu",
        "1qgk",
        "1qiu",
        "1qku",
        "1qle",
        "1qm2",
        "1qml",
        "1qmz",
        "1qnv",
        "1qqw",
        "1qu1",
        "1qys",
        "1r4i",
        "1r4n",
        "1r4u",
        "1r5p",
        "1r7r",
        "1r8j",
        "1r9f",
        "1rcx",
        "1rd8",
        "1rf8",
        "1rfb",
        "1rh4",
        "1ri1",
        "1rlc",
        "1ron",
        "1rtn",
        "1ruz",
        "1rv1",
        "1rva",
        "1rvc",
        "1rvf",
        //  "1rw6",
        //     Entry 1RW6 was removed from the distribution of released PDB entries (status Obsolete) on 2011-07-13.
        //    It has been replaced (superseded) by 3NYL.
        "1rwt",
        "1ryf",
        "1rys",
        "1s58",
        "1s5l",
        "1s9v",
        "1sbt",
        "1sep",
        "1set",
        "1sfc",
        "1sg1",
        "1sgz",
        "1shr",
        "1si3",
        "1skh",
        "1sl2",
        "1smd",
        "1sos",
        "1st0",
        "1su4",
        "1sva",
        "1svm",
        "1szp",
        "1t24",
        "1t25",
        "1t2k",
        "1t6o",
        "1t8u",
        "1tau",
        "1tbd",
        "1tbg",
        "1tcf",
        "1tcr",
        "1tez",
        "1tf6",
        "1tgh",
        "1tgs",
        "1tha",
        "1thj",
        "1thv",
        "1tki",
        "1tlf",
        "1tll",
        "1trz",
        "1ttd",
        "1ttt",
        "1tub",
        "1tui",
        "1tup",
        "1u04",
        "1u19",
        "1u1z",
        "1u6b",
        "1u8d",
        "1ubq",
        "1ui9",
        "1ul1",
        "1um2",
        "1ump",
        "1un6",
        "1ut0",
        "1uv6",
        "1uwh",
        "1v0d",
        "1vas",
        "1vf5",
        "1vol",
        "1vpe",
        "1vpr",
        "1w0e",
        "1w36",
        "1w3b",
        "1w6k",
        "1w85",
        "1wa5",
        "1wb1",
        "1wdw",
        "1wfb",
        "1wio",
        "1wkw",
        "1wq1",
        "1www",
        "1x70",
        "1xf0",
        //   "1xi4",  PROBLEMS!!
        "1xka",
        "1xkk",
        "1xmb",
        "1xnj",
        "1xp0",
        "1xtc",
        "1y0j",
        "1y26",
        "1y27",
        "1ya5",
        "1ycq",
        "1ycr",
        "1yet",
        "1yfg",
        "1ygp",
        "1yhu",
        "1yi5",
        "1ykf",
        "1ylv",
        "1ymb",
        "1ymg",
        "1ynw",
        "1ytb",
        "1ytf",
        "1yvn",
        "1yyf",
        "1z1g",
        "1z2b",
        "1z6t",
        "1z7g",
        "1zaa",
        "1zcd",
        "1zen",
        "1zes",
        "1znf",
        "1zqa",
        "2a1s",
        "2a3d",
        "2a45",
        "2a7u",
        "2aai",
        "2ahm",
        "2am9",
        "2amb",
        "2ayh",
        "2az8",
        "2az9",
        "2azc",
        "2b3y",
        "2b4n",
        "2bat",
        "2bbm",
        "2beg",
        "2bg9",
        "2biw",
        "2bku",
        "2bpt",
        "2brz",
        "2btv", // PROBLEMS (50K at limit)
        "2buk",
        "2c2a",
        "2c37", // PROBLEMS 46K
        "2cag",
        "2cas",
        "2cbj",
        "2cf2",
        "2cg9",
        "2cha",
        "2ckb",
        "2cpk",
        "2crd",
        "2cts",
        "2d04",
        "2d1s",
        "2d1t",
        "2d81",
        "2dcg",
        "2dez",
        "2dhb",
        "2dhc",
        "2dln",
        "2drd",
        "2e4z",
        "2ebt",
        "2eq7",
        "2eud",
        "2euf",
        "2ez6",
        "2ezo",
        "2f1m",
        "2f8s",
        "2fae",
        "2ffl",
        "2fk6",
        "2fp4",
        "2frv",
        "2g19",
        "2g1m",
        "2g30",
        "2gbl",
        "2gfp",
        "2gg4",
        "2gga",
        "2ggd",
        "2ghw",
        "2gls",
        "2gs6",
        "2gtl",
        "2h4f",
        "2h59",
        "2h5o",
        "2h5q",
        "2h7d",
        "2h9r",
        "2hac",
        "2hbs",
        "2hck",
        "2hco",
        "2hft",
        "2hgh",
        "2hgt",
        "2hhb",
        "2hil",
        "2hiu",
        "2hla",
        "2hmi",
        "2hu4",
        "2i8b",
        "2ic8",
        "2ifq",
        "2ioq",
        "2ipy",
        "2irv",
        "2j0d",
        "2j1u",
        "2j67",
        "2j7w",
        "2jgd",
        "2jho",
        "2jlb",
        "2jzq",
        "2k6o",
        "2k9j",
        "2ka6",
        "2kh2",
        "2kin",
        "2kj3",
        "2kod",
        "2kz1",
        "2l63",
        "2l7u",
        "2lhb",
        "2lm3",
        "2lmn",
        "2lmp",
        "2lyz",
        "2m4j",
        "2mfr",
        "2mys",
        "2n5e",
        "2ncd",
        "2nll",
        "2nn6",
        "2nrf",
        "2nrl",
        "2nrm",
        "2nse",
        "2nwx",
        "2o0c",
        "2o1u",
        "2o1v",
        "2o39",
        "2o60",
        "2o61",
        "2o6g",
        "2oar",
        "2oau",
        "2obs",
        "2om3",
        "2one",
        "2onj",
        "2oq1",
        "2or1",
        "2ozo",
        "2p04",
        "2p1h",
        "2p1n",
        "2p1p",
        "2p1q",
        "2pah",
        "2pd7",
        "2pel",
        "2pf2",
        "2pgi",
        "2pi0",
        "2plv",
        "2pne",
        "2pqb",
        "2ptc",
        "2ptn",
        "2pyp",
        "2q3z",
        "2q57",
        "2q66",
        "2qbz",
        "2qkm",
        "2qrv",
        "2qw7",
        "2r4r",
        "2r6p",
        "2rh1",
        "2rik",
        "2rnm",
        "2sod",
        "2src",
        "2taa",
        "2tbv",  // PROBLEMS with "nextCarbon" ??
        "2tmv",
        "2toh",
        "2tra",
        "2uwm",
        "2v01",
        "2vaa",
        "2vab",
        "2vbc",
        "2vdo",
        "2vir",
        "2vv5",
        "2wj7",
        "2xow",
        "2y0g",
        "2yhx",
        "2ymk",
        "2ypi",
        "2yye",
        "2z6c",
        "2z75",
        "2z7x",
        "2zb5",
        "2zib",
        "2zoi",
        "2zta",
        "2zxe",
        "2zyv",
        "309d",
        "3a3y",
        "3ado",
        "3alz",
        "3amr",
        "3b43",
        "3b4r",
        "3b75",
        "3b7e",
        "3b8c",
        "3b8e",
        "3bc8",
        "3bes",
        "3bgf",
        "3bik",
        "3biy",
        "3blw",
        "3bn4",
        "3bp5",
        "3by8",
        "3c6g",
        "3cap",
        "3ciy",
        "3cl0",
        "3cln",
        "3cmp",
        "3cmx",  // causes a CRASH!
        "3cna",
        "3cpa",
        "3cpp",
        "3csh",
        "3csy",
        "3cyt",
        "3cyu",
        "3d6n",
        "3dag",
        "3dfr",
        "3dge",
        "3dkt",
        "3e7t",
        "3ert",
        "3est",
        "3et6",
        "3ets",
        "3eub",
        "3eyc",
        "3f3e",
        "3f47",
        //   "3fcs", // "nextCarbon" problem
        "3fke",
        "3frh",
        "3fsn",
        "3fvy",
        "3fw4",
        "3fxi",
        "3g60",
        "3g61",
        "3gbi",
        "3gpd",
        "3gss",
        "3gwv",
        "3h1j",
        "3h47",
        "3hfl",
        "3hfm",
        "3hhr",
        "3hls",
        "3hqr",
        "3hvt",
        "3hz3",
        "3i0g",
        "3icd",
        "3inb",
        "3iol",
        "3irw",
        "3iwm",
        "3iwn",
        "3ixz",
        "3iyq",
        "3iyr",
        "3iz4",
        // "3iz8",
        // Entry 3IZ8 was removed from the distribution of released PDB entries (status Obsolete) on 2014-12-10.
        //It has been replaced (superseded) by 4V4L.
        "3j1t",
        "3j2u",
        // "3j3q", no pdb file too big
        // "3j3y",
        "3j5m",
        "3j6r",
        "3jac",
        "3jad",
        // "3jb9",  // 86K atoms
        // "3jc8",
        // "3jc9",
        "3kg2",
        "3kin",
        "3kll",
        "3kud",
        "3kyh",
        "3l1e",
        // "3l5q", obsolete
        "3lcb",
        "3ldh",
        "3loh",
        "3lpw",
        "3lqq",
        "3ly6",
        "3m24",
        "3m7r",
        "3m9s",
        "3mge",
        "3mmj",
        "3mon",
        "3n0g",
        "3nhc",
        "3nir",
        "3nkx",
        "3og7",
        "3os0",
        "3os1",
        "3p05",
        "3p5p",
        "3pb3",
        "3pe4",
        "3pgk",
        "3pgm",
        "3pgt",
        "3pqr",
        "3psg",
        "3pt6",
        "3pte",
        "3q6e",
        "3q6x",
        "3r1k",
        "3r1r",
        "3r8f",
        "3rgk",
        "3rh8",
        //  "3rif",  OOM error PROBLEM
        "3rko",
        "3rui",
        "3rxw",
        "3s0x",
        "3sdp",
        "3sdy",
        "3se7",
        "3sfz",
        "3sm5",
        "3srp",
        "3tnp",
        "3tom",
        "3tt1",
        "3tt3",
        "3twy",
        "3u5d",
        "3u5e",
        "3u5z",
        "3ugm",
        "3um7",
        "3unf",
        "3uus",
        "3ux4",
        "3v3b",
        "3v6o",
        "3v6t",
        "3vcd",
        "3vdx",
        "3vie",
        "3vkh",
        "3vne",
        "3vrf",
        "3vzs",
        "3w14",
        "3wwj",
        "3zdo",
        "3zmk",
        "3zml",
        "3zoj",
        "3zpk",
        "4ajx",
        "4ald",
        "4ape",
        "4ar7",
        "4awb",
        "4blm",
        "4c6i",
        "4cc8",
        "4cms",
        "4cox",
        "4djh",
        "4dkl",
        "4dpv",
        "4ea3",
        "4eeu",
        "4egg",
        "4ej4",
        "4ers",
        "4esv",
        "4eyl",
        "4fgu",
        "4fqi",
        "4fyw",
        "4g1g",
        "4gbc",
        "4gcr",
        "4gcz",
        "4gjt",
        "4gsl",
        "4hfe",
        "4hhd",
        "4iao",
        "4iar",
        "4ib4",
        "4icd",
        "4imv",
        "4ins",
        "4iyf",
        "4j7u",
        "4jhw",
        "4jj0",
        "4kbp",
        "4kf5",
        "4kqw",
        "4kqx",
        "4kzd",
        "4l6r",
        "4ldb",
        "4ldd",
        "4lp5",
        "4lsx",
        "4m48",
        "4m4w",
        "4mmv",
        "4mn8",
        "4mne",
        "4n6o",
        "4nco",
        "4o9c", // OOM???
        "4oaa",
        "4oo8",
        "4or2",
        "4ow0",
        "4p1w",
        "4p6i",
        "4pe5",
        "4pfk",
        "4prq",
        "4pti",
        "4pyp",
        "4q21",
        "4qb0",
        "4qqw",
        "4qyz",
        "4r8f",
        "4rhv",
        "4rxn",
        "4tna",
        "4tpw",
        "4ts2",
        // "4tvx",
        "4u5c",
        "4ued",
        "4uft",
        "4un3",
        "4ur0",
        "4uww",
        // "4V60",
        // "4v6m", // no PDB file , too big Atom Count: 163040 !!
        // "4v6t",
        // "4v8q",
        "4xia",
        "4xmm",
        "4xr8",
        "4xv1",
        "4xv2",
        // "4xxb",
        "4yb9",
        "4ybq",
        "4yoi",
        "4z62",
        "4z63",
        "4z64",
        "4zhd",
        "4zpr",
        "4zqk",
        "4zwc",
        // "5a1a", // OOM??  Yes there is a problem here
        "5a22",
        "5ara",
        "5at1",
        "5bjp",
        "5btr",
        "5cfs",
        "5cfu",
        "5chb",
        "5dis",
        "5dk3",
        "5dou",
        "5e33",
        "5e4v",
        "5e54",
        "5eqi",
        "5et3",
        "5g1n",
        //  "5gar",  // OOM problems??
        "5ire",
        "5j7v",
        "5j89",
        "5jh9",
        "5jm0",
        "5jxe",
        "5ks9",
        "5kuf",
        "5l2s",
        "5l2t",
        "5lf5",
        "5lpb",
        "5m2g",
        "5m8u",
        "5m92",
        "5mdh",
        "5mf6",
        // "5mnj",
        "5nij",
        "5oeh",
        "5ox6",
        "5p21",
        "5pep",
        "5rsa",
        "5swd",
        "5swe",
        "5t46",
        "5t6o",
        "5tus",
        "5tzo",
        "5ucw",
        "5vb8",
        // "5vkq", // OOM problem
        //  "5vox", // NEXT oxygen problem
        //  "5voy",  // skip for now...
        //   "5voz",
        "5w1o",
        "5weo",
        //    "5xnl", // 99K atoms!
        "5yxw",
        "5z10",
        //    "5zji", // OOM
        "6a95",
        "6adh",
        "6am8",
        "6b3r",
        "6bpz",
        "6bt3",
        // "6by7",
        "6c63",
        "6c6k",
        "6cfz",
        "6clz",
        // "6crz", // OOM
        "6d6v",
        "6db8",
        "6gpb",
        "6j4y",
        "6j8j",
        //    "6kac", // 77K
        // "6kad",
        "6ldh",
        "6lu7",
        "6m17",
        "6mam",
        "6n7p",
        //"6nwa", // Atom Count: 137509 !!
        //  "6o81",  // OOM
        "6o85",
        //    "6o9z", // OOM */
        "6p6w",
        "6pfk",
        "6tna",
        "6vsb",
        "6vxx",
        "6w41",
        "6w4x",
        "6x2g",
        "6yyt",
        "7acn",
        "7bv2",
        "7bzf",
        "7c2k",
        "7dfr",
        "7hvp",
        "8cat",
        "8gpb",
        "8icd",
        "8ruc",
        "9icd",
        "9pap",
        "9rub"

    )

}