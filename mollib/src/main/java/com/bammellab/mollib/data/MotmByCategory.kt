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

package com.bammellab.mollib.data

object MotmByCategory {

    val motmTabLabels = listOf(
            "Health and Disease",
            "Life",
            "Biotech/Nanotech",
            "Structures",
            "All"
    )

    val MotmCategoryHealth = arrayOf(

            "Section Health and Disease",
            "You and Your Health",
            "156",
            "54",
            "13",
            "79",
            "189",
            "92",
            "177",
            "115",
            "51",
            "49",
            "66",
            "56",
            "57",
            "97",
            "4",
            "127",
            "82",
            "139",
            "45",
            "257",
            "83",
            "138",
            "52",
            "41",
            "151",
            "240",
            "14",
            "149",
            "9",
            "62",
            "68",
            "217",
            "96",
            "12",
            "61",
            "239",
            "223",
            "101",
            "158",
            "233",
            "104",
            "164",
            "53",
            "37",
            "213",
            "94",
            "63",
            "188",
            "25",
            "75",
            "209",
            "155",
            "117",
            "269",  // Nicotine, Cancer, and Addiction
            "278",  // SARS-CoV-2 Nucleocapsid and Home Tests
            "288",  // Vitamins
            "291",  // Hyaluronidases
            "293",  // CFTR and Cystic Fibrosis
            "298",  // Angiotensin and Blood Pressure
            "311",  // GLP-1 Receptor Agonists
            "312",  // FOXP3

            "Immune System",
            "21",
            "170",
            "214",
            "162",
            "178",
            "76",
            "113",
            "128",
            "224",
            "9",
            "62",
            "231",
            "136",
            "204",
            "256",
            "193",
            "98",
            "63",
            "209",
            "143",
            "272",  // Secretory Antibodies
            "278",  // SARS-CoV-2 Nucleocapsid and Home Tests
            "280",  // MHC I Peptide Loading Complex
            "286",  // RSV Fusion Glycoprotein
            "287",  // ZAR1 Resistosome
            "312",  // FOXP3

            "HIV and AIDS",
            "170",
            "181",
            "163",
            "169",
            "33",
            "6",
            "135",
            "63",

            "Diabetes",
            "194",
            "202",
            "184",
            "77",
            "208",
            "14",
            "182",
            "186",
            "310",  // Incretins
            "311",  // GLP-1 Receptor Agonists

            "Cancer",
            "160",
            "177",
            "56",
            "214",
            "255",
            "236",
            "34",
            "126",
            "45",
            "212",
            "252", // DEC 2020 HepC
            "108",
            "221",
            "230",
            "62",
            "234",
            "175",
            "237",
            "31",
            "204",
            "195",
            "148",
            "172",
            "238",
            "47",
            "98",
            "245",
            "43",
            "227",
            "91",
            "73",
            "267",  // VEGF and Angiogenesis
            "268",  // HER2/neu and Trastuzumab
            "269",  // Nicotine, Cancer, and Addiction
            "270",  // Pyruvate Kinase M2
            "271",  // Non-Homologous End Joining Supercomplexes
            "279",  // Anaphase-Promoting Complex / Cyclosome
            "283",  // c-Abl Protein Kinase and Imatinib
            "284",  // ATM and ATR Kinases
            "285",  // Histone Deacetylases

            "Viruses",
            "132",
            "2",
            "170",
            "242",
            "103",
            "178",
            "76",
            "252", // DEC 2020
            "163",
            "169",
            "33",
            "221",
            "113",
            "135",
            "231",
            "125",
            "20",
            "200",
            "249",
            "256",
            "246",
            "47",
            "98",
            "109",
            "197",
            "258",  // Glucocorticoid Receptor and Dexamethasone
            "278",  // SARS-CoV-2 Nucleocapsid and Home Tests
            "286",  // RSV Fusion Glycoprotein
            "292",  // YES Complex
            "302",  // H5 Hemagglutinin
            "306",  // Eastern Equine Encephalitis Virus
            "308",  // Arc

            "Toxins and Poisons",
            "71",
            "54",
            "19",
            "28",
            "173",
            "69",
            "82",
            "58",
            "212",
            "41",
            "196",
            "239",
            "38",
            "161",
            "40",
            "118",
            "73",
            "310",  // Incretins

            "Drug Action",
            "160",
            "100",
            "146",
            "226",
            "115",
            "97",
            "17",
            "82",
            "34",
            "45",
            "191",
            "212",
            "169",
            "113",
            "135",
            "128",
            "102",
            "175",
            "95",
            "171",
            "187",
            "133",
            "217",
            "123",
            "29",
            "121",
            "164",
            "188",
            "192",
            "258",  // Glucocorticoid Receptor and Dexamethasone
            "276",  // Click Chemistry
            "279",  // Anaphase-Promoting Complex / Cyclosome
            "285",  // Histone Deacetylases
            "307",  // Capturing Beta-Lactamase in Action
            "311",  // GLP-1 Receptor Agonists

            "Antimicrobial Resistance",
            "146",
            "226",
            "34",
            "95",
            "187",
            "29",
            "121",
            "188",
            "192",
            "307",  // Capturing Beta-Lactamase in Action

            "Drugs and the Brain",
            "71",
            "54",
            "100",
            "235",
            "51",
            "250",
            "58",
            "191",
            "247",
            "171",
            "217",
            "223",
            "38",
            "147",
            "164",
            "167",
            "118",
            "308",  // Arc

            "Coronavirus",
            "242",
            "249",
            "256",
            "246",
            "258",  // Glucocorticoid Receptor and Dexamethasone
            "264",  // SARS-CoV-2 Spike Variants
            "278",  // SARS-CoV-2 Nucleocapsid and Home Tests

            "Infectious Disease",
            "294",  // Injectisome
            "299",  // Malaria Parasite PTEX
            "302",  // H5 Hemagglutinin

            "Peak Performance",
            "263",  // Acetohydroxyacid Synthase
            "269",  // Nicotine, Cancer, and Addiction
            "273",  // Respiratory Supercomplex
            "282",  // Odorant Receptors
            "288",  // Vitamins
            "298",  // Angiotensin and Blood Pressure

            "Vaccines",
            "262",  // Fifty Years of Open Access to PDB Structures
            "286",  // RSV Fusion Glycoprotein
            "306"   // Eastern Equine Encephalitis Virus

    )

    val MotmCategoryLife = arrayOf(

            "Section Molecules of Life",
            "Protein Synthesis",
            "80",
            "89",
            "210",
            "16",
            "181",
            "48",
            "32",
            "23",
            "168",
            "55",
            "139",
            "3",
            "81",
            "122",
            "45",
            "86",
            "41",
            "33",
            "108",
            "230",
            "131",
            "39",
            "9",
            "145",
            "7",
            "141",
            "112",
            "31",
            "106",
            "166",
            "172",
            "8",
            "140",
            "10",
            "121",
            "130",
            "40",
            "104",
            "65",
            "213",
            "150",
            "98",
            "67",
            "91",
            "73",
            "15",
            "157",
            "84",
            "60",
            "87",
            "260",  // Ribonuclease P
            "266",  // Oligosaccharyltransferase
            "289",  // Mediator
            "295",  // Ribosome Diversity

            "Enzymes",
            "80",
            "156",
            "54",
            "89",
            "13",
            "74",
            "16",
            "92",
            "215",
            "198",
            "115",
            "152",
            "49",
            "66",
            "56",
            "57",
            "93",
            "154",
            "17",
            "82",
            "220",
            "34",
            "55",
            "218",
            "86",
            "90",
            "138",
            "77",
            "30",
            "24",
            "50",
            "33",
            "111",
            "151",
            "129",
            "102",
            "224",
            "78",
            "9",
            "179",
            "187",
            "26",
            "141",
            "96",
            "12",
            "61",
            "225",
            "106",
            "153",
            "8",
            "140",
            "105",
            "238",
            "121",
            "40",
            "11",
            "104",
            "65",
            "43",
            "116",
            "94",
            "188",
            "25",
            "91",
            "209",
            "73",
            "84",
            "46",
            "117",
            "263",  // Acetohydroxyacid Synthase
            "277",  // Plastic-eating Enzymes
            "301",  // Assembly Line Polyketide Synthases

            "Molecular Infrastructure",
            "19",
            "99",
            "254",
            "4",
            "134",
            "175",
            "211",
            "232",
            "209",
            "185",
            "114",
            "275",  // Actin Branching by Arp2/3 Complex
            "291",  // Hyaluronidases
            "296",  // ESCRT-III

            "Transport",
            "203",
            "92",
            "173",
            "88",
            "159",
            "35",
            "257",
            "208",
            "41",
            "85",
            "95",
            "237",
            "171",
            "205",
            "123",
            "158",
            "233",
            "37",
            "118",
            "265",  // Golgi Casein Kinase
            "290",  // Nanowires
            "293",  // CFTR and Cystic Fibrosis
            "294",  // Injectisome
            "305",  // TOC-TIC Translocon

            "Biological Energy",
            "89",
            "13",
            "74",
            "72",
            "27",
            "198",
            "216",
            "93",
            "154",
            "144",
            "137",
            "36",
            "5",
            "90",
            "174",
            "24",
            "50",
            "42",
            "240",
            "129",
            "102",
            "78",
            "1",
            "244",
            "22",
            "183",
            "153",
            "147",
            "46",
            "270",  // Pyruvate Kinase M2
            "273",  // Respiratory Supercomplex
            "281",  // Cellulases and Bioenergy

            "Molecules and the Environment",
            "216",
            "254",
            "220",
            "212",
            "201",
            "179",
            "26",
            "22",
            "59",
            "225",

            "Biology of Plants",
            "110",
            "27",
            "49",
            "66",
            "254",
            "124",
            "137",
            "218",
            "90",
            "42",
            "201",
            "199",
            "26",
            "244",
            "22",
            "59",
            "183",
            "248",
            "8",
            "161",
            "11",
            "180",
            "263",  // Acetohydroxyacid Synthase
            "274",  // Phytohormone Receptor DWARF14
            "287",  // ZAR1 Resistosome
            "297",  // Carbon Capture Mechanisms
            "305",  // TOC-TIC Translocon
            "309",  // Abscisic Acid Receptor

            "Molecular Motors",
            "72",
            "176",
            "64",
            "18",
            "211",
            "219",
            "300",  // Flagellar Motor
            "305",  // TOC-TIC Translocon

            "Cellular Signaling",
            "71",
            "54",
            "100",
            "235",
            "92",
            "110",
            "51",
            "44",
            "152",
            "250",
            "236",
            "17",
            "194",
            "202",
            "126",
            "45",
            "58",
            "184",
            "191",
            "52",
            "14",
            "182",
            "128",
            "149",
            "107",
            "171",
            "68",
            "133",
            "217",
            "204",
            "207",
            "183",
            "248",
            "223",
            "38",
            "195",
            "148",
            "186",
            "147",
            "164",
            "167",
            "43",
            "75",
            "190",
            "258",  // Glucocorticoid Receptor and Dexamethasone
            "267",  // VEGF and Angiogenesis
            "268",  // HER2/neu and Trastuzumab
            "269",  // Nicotine, Cancer, and Addiction
            "274",  // Phytohormone Receptor DWARF14
            "279",  // Anaphase-Promoting Complex / Cyclosome
            "282",  // Odorant Receptors
            "283",  // c-Abl Protein Kinase and Imatinib
            "284",  // ATM and ATR Kinases
            "298",  // Angiotensin and Blood Pressure
            "310",  // Incretins

            "Nucleic Acids",
            "71",
            "54",
            "210",
            "100",
            "51",
            "119",
            "23",
            "229",
            "58",
            "191",
            "171",
            "217",
            "223",
            "38",
            "147",
            "10",
            "121",
            "130",
            "65",
            "164",
            "98",
            "167",
            "118",
            "227",
            "91",
            "15",
            "157",
            "260",  // Ribonuclease P
            "284",  // ATM and ATR Kinases
            "312",  // FOXP3
            "313",  // Natural RNA-Only Assemblies

            "Bioluminescence and Fluorescence",
            "229",
            "174",
            "42",

            "Molecular Evolution",
            "120",
            "124",
            "36",
            "5",
            "34",
            "228",
            "206",
            "78",
            "125",
            "29",
            "121",
            "84",
            "192",
            "155",
            "295",  // Ribosome Diversity
            "308",  // Arc

            "Central Dogma",
            "16",
            "255",
            "23",
            "3",
            "121",
            "40",
            "249",
            "245",
            "15",
            "271",  // Non-Homologous End Joining Supercomplexes
            "285",  // Histone Deacetylases
            "289",  // Mediator

            "Molecules for a Sustainable Future",
            "274",  // Phytohormone Receptor DWARF14
            "277",  // Plastic-eating Enzymes
            "297"   // Carbon Capture Mechanisms
    )

    val MotmCategoryBiotech = arrayOf(

            "Section Biotech and Nanotech",

            "Recombinant DNA",
            "198",
            "55",
            "3",
            "112",
            "8",

            "Biotechnology",
            "13",
            "74",
            "120",
            "110",
            "216",
            "214",
            "228",
            "3",
            "218",
            "174",
            "77",
            "42",
            "199",
            "12",
            "225",
            "261",  // DNA-Sequencing Nanopores
            "277",  // Plastic-eating Enzymes

            "Nanotechnology",
            "132",
            "21",
            "181",
            "124",
            "137",
            "119",
            "165",
            "70",
            "229",
            "174",
            "42",
            "131",
            "237",
            "232",
            "222",
            "161",
            "180",
            "261",  // DNA-Sequencing Nanopores
            "290",  // Nanowires
            "301",  // Assembly Line Polyketide Synthases

            "Renewable Energy",
            "228",
            "111",
            "179",
            "281"   // Cellulases and Bioenergy
    )

    val MotmCategoryStructures = arrayOf(

            "Section Structures and Structure Determination",

            "Biomolecules",
            "89",
            "210",
            "215",
            "4",
            "124",
            "127",
            "23",
            "206",
            "41",
            "14",
            "200",
            "259",  // Designed Proteins and Citizen Science
            "262",  // Fifty Years of Open Access to PDB Structures

            "Biomolecular Structural Biology",
            "210",
            "9",
            "1",
            "237",
            "142",
            "12",
            "207",
            "105",
            "104",
            "46",
            "241", // 20 years of molecules
            "262",  // Fifty Years of Open Access to PDB Structures
            "303",  // Enoyl-CoA Carboxylases/Reductases
            "304",  // Apolipoprotein B-100 and LDL Receptor
            "308",  // Arc
            "310",  // Incretins
            "311",  // GLP-1 Receptor Agonists
            "313",  // Natural RNA-Only Assemblies

            "Hybrid Methods",
            "189",
            "198",
            "88",
            "163",
            "169",
            "175",
            "205",
            "211",
            "166",
            "219",

            "Nobel Prizes and PDB Structures",
            "100",
            "203",
            "21",
            "173",
            "72",
            "27",
            "181",
            "97",
            "228",
            "23",
            "58",
            "41",
            "252", // DEC 2020 HepC
            "240",
            "1",
            "142",
            "22",
            "59",
            "38",
            "105",
            "10",
            "121",
            "40",
            "245",
            "227",
            "109",
            "276",  // Click Chemistry
            "312",  // FOXP3

            "PDB Data",
            "262",  // Fifty Years of Open Access to PDB Structures

            "Protein Structure Prediction",
            "259",  // Designed Proteins and Citizen Science
            "287"   // ZAR1 Resistosome

    )
}