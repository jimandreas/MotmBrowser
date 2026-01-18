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
            "71",   // Acetylcholine Receptor
            "54",   // Acetylcholinesterase
            "100",  // Adrenergic Receptors
            "235",  // AMPA Receptor
            "308",  // Arc
            "51",   // Calcium Pump
            "250",  // Capsaicin Receptor TRPV1
            "58",   // G Proteins
            "247",  // Myelin-associated Glycoprotein
            "217",  // Opioid Receptors
            "223",  // Piezo1 Mechanosensitive Channel
            "118",  // Sodium-Potassium Pump
            "243",  // Voltage-gated Sodium Channels

            "Coronavirus",
            "242",
            "249",
            "256",
            "246",
            "258",  // Glucocorticoid Receptor and Dexamethasone
            "264",  // SARS-CoV-2 Spike Variants
            "278",  // SARS-CoV-2 Nucleocapsid and Home Tests

            "Infectious Disease",
            "146",  // Aminoglycoside Antibiotics
            "226",  // Aminoglycoside Antibiotics and Resistance
            "28",   // Anthrax Toxin
            "69",   // Cholera Toxin
            "97",   // Circadian Clock Proteins
            "302",  // H5 Hemagglutinin
            "294",  // Injectisome
            "299",  // Malaria Parasite PTEX
            "187",  // New Delhi Metallo-Beta-Lactamase
            "29",   // Penicillin-binding Proteins
            "211",  // Pilus Machine
            "143",  // Toll-like Receptors
            "192",  // Vancomycin

            "Peak Performance",
            "263",  // Acetohydroxyacid Synthase
            "100",  // Adrenergic Receptors
            "13",   // Alcohol Dehydrogenase
            "92",   // Anabolic Steroids
            "298",  // Angiotensin and Blood Pressure
            "72",   // ATP Synthase
            "51",   // Calcium Pump
            "49",   // Carbonic Anhydrase
            "57",   // Catalase
            "97",   // Circadian Clock Proteins
            "154",  // Citrate Synthase
            "144",  // Cytochrome c
            "5",    // Cytochrome c Oxidase
            "45",   // Estrogen Receptor
            "90",   // Fatty Acid Synthase
            "24",   // Glycolytic Enzymes
            "50",   // Glutamine Synthetase
            "52",   // Growth Hormone
            "41",   // Hemoglobin
            "240",  // Hypoxia-Inducible Factors
            "102",  // Lac Repressor
            "196",  // Lead Poisoning
            "149",  // Leptin
            "199",  // Monellin
            "18",   // Myosin
            "269",  // Nicotine, Cancer, and Addiction
            "282",  // Odorant Receptors
            "217",  // Opioid Receptors
            "96",   // Pepsin-like Aspartic Proteases
            "153",  // Proteasome
            "273",  // Respiratory Supercomplex
            "147",  // Rhodopsin
            "233",  // S-Nitrosylated Hemoglobin
            "164",  // Serotonin Receptor
            "118",  // Sodium-Potassium Pump
            "94",   // Thrombin
            "155",  // Vitamin D Receptor
            "288",  // Vitamins

            "Vaccines",
            "132",  // Adenovirus
            "170",  // Broadly Neutralizing Antibodies
            "103",  // Designer Insulin
            "306",  // Eastern Equine Encephalitis Virus
            "262",  // Fifty Years of Open Access to PDB Structures
            "169",  // HIV Envelope Glycoprotein
            "6",    // HIV-1 Protease
            "221",  // Human Papillomavirus and Vaccines
            "113",  // Influenza Neuraminidase
            "231",  // Nanobodies
            "125",  // Parvoviruses
            "20",   // Poliovirus and Rhinovirus
            "161",  // Ricin
            "286",  // RSV Fusion Glycoprotein
            "246",  // SARS-CoV-2 Spike
            "256"   // SARS-CoV-2 Spike and Antibodies

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
            "253",  // Expressome
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
            "309",  // Abscisic Acid Receptor
            "263",  // Acetohydroxyacid Synthase
            "110",  // Auxin and TIR1 Ubiquitin Ligase
            "27",   // Bacteriorhodopsin
            "297",  // Carbon Capture Mechanisms
            "49",   // Carbonic Anhydrase
            "66",   // Carbonic Anhydrase II
            "254",  // Cellulose Synthase
            "124",  // Concanavalin A and Circular Permutation
            "137",  // Cytochrome bc1
            "218",  // EPSP Synthase and Weedkillers
            "90",   // Fatty Acid Synthase
            "42",   // Green Fluorescent Protein (GFP)
            "201",  // Isoprene Synthase
            "199",  // Monellin
            "26",   // Nitrogenase
            "244",  // Phycobilisome
            "22",   // Photosystem I
            "59",   // Photosystem II
            "183",  // Phytochrome
            "274",  // Phytohormone Receptor DWARF14
            "248",  // Phototropin
            "161",  // Ricin
            "11",   // Rubisco
            "180",  // TAL Effectors
            "305",  // TOC-TIC Translocon
            "287",  // ZAR1 Resistosome

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
            "71",   // Acetylcholine Receptor
            "54",   // Acetylcholinesterase
            "251",  // Adenylyl Cyclase
            "100",  // Adrenergic Receptors
            "235",  // AMPA Receptor
            "92",   // Anabolic Steroids
            "279",  // Anaphase-Promoting Complex / Cyclosome
            "298",  // Angiotensin and Blood Pressure
            "284",  // ATM and ATR Kinases
            "110",  // Auxin and TIR1 Ubiquitin Ligase
            "283",  // c-Abl Protein Kinase and Imatinib
            "51",   // Calcium Pump
            "44",   // Calmodulin
            "152",  // Caspase-Activated DNase
            "250",  // Capsaicin Receptor TRPV1
            "236",  // Cyclin and Cyclin-dependent Kinase
            "17",   // Cytochrome P450
            "194",  // Designer Insulins
            "202",  // Dipeptidyl Peptidase 4
            "126",  // Epidermal Growth Factor
            "45",   // Estrogen Receptor
            "58",   // G Proteins
            "184",  // Glucagon
            "258",  // Glucocorticoid Receptor and Dexamethasone
            "191",  // GPCR Kinases
            "52",   // Growth Hormone
            "268",  // HER2/neu and Trastuzumab
            "310",  // Incretins
            "14",   // Insulin
            "182",  // Insulin Receptor
            "128",  // Interferons
            "149",  // Leptin
            "107",  // Lipid-linked Oligosaccharide
            "171",  // Neurotransmitter Transporters
            "68",   // Nitric Oxide Synthase
            "269",  // Nicotine, Cancer, and Addiction
            "133",  // Nuclear Receptors
            "282",  // Odorant Receptors
            "217",  // Opioid Receptors
            "204",  // PD-1 (Programmed Cell Death Protein 1)
            "207",  // Photoactive Yellow Protein
            "183",  // Phytochrome
            "274",  // Phytohormone Receptor DWARF14
            "248",  // Phototropin
            "223",  // Piezo1 Mechanosensitive Channel
            "38",   // Potassium Channels
            "195",  // RAF Protein Kinases
            "148",  // Ras Protein
            "186",  // Receptor for Advanced Glycation End Products
            "147",  // Rhodopsin
            "164",  // Serotonin Receptor
            "167",  // Ser/Thr Protein Phosphatases
            "43",   // Src Tyrosine Kinase
            "75",   // Tumor Necrosis Factor
            "190",  // Ubiquitin
            "267",  // VEGF and Angiogenesis
            "155",  // Vitamin D Receptor
            "243",  // Voltage-gated Sodium Channels

            "Nucleic Acids",
            "7",    // Nucleosome
            "8",    // Restriction Enzymes
            "10",   // Ribosomal Subunits
            "15",   // Transfer RNA
            "23",   // DNA
            "65",   // Self-splicing RNA
            "91",   // Thymine Dimers
            "98",   // Small Interfering RNA (siRNA)
            "119",  // Designed DNA Crystal
            "121",  // Ribosome
            "130",  // Riboswitches
            "155",  // Vitamin D Receptor
            "157",  // Transfer-Messenger RNA
            "172",  // RecA and Rad51
            "210",  // Adenine Riboswitch in Action
            "227",  // Telomerase
            "229",  // Fluorescent RNA Aptamers
            "245",  // Spliceosomes
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
            "16",   // Aminoacyl-tRNA Synthetases
            "255",  // Cisplatin and DNA
            "23",   // DNA
            "3",    // DNA Polymerase
            "253",  // Expressome
            "285",  // Histone Deacetylases
            "289",  // Mediator
            "271",  // Non-Homologous End Joining Supercomplexes
            "121",  // Ribosome
            "40",   // RNA Polymerase
            "249",  // SARS-CoV-2 RNA-dependent RNA Polymerase
            "245",  // Spliceosomes
            "15",   // Transfer RNA

            "Molecules for a Sustainable Future",
            "216",  // Biodegradable Plastic
            "297",  // Carbon Capture Mechanisms
            "254",  // Cellulose Synthase
            "220",  // Dehalogenases
            "212",  // Glutathione Transferases
            "201",  // Isoprene Synthase
            "179",  // Methyl-coenzyme M Reductase
            "26",   // Nitrogenase
            "22",   // Photosystem I
            "59",   // Photosystem II
            "225",  // Phytase
            "274",  // Phytohormone Receptor DWARF14
            "277"   // Plastic-eating Enzymes
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
            "13",   // Alcohol Dehydrogenase
            "74",   // Alpha-amylase
            "120",  // Antifreeze Proteins
            "110",  // Auxin and TIR1 Ubiquitin Ligase
            "216",  // Biodegradable Plastic
            "181",  // Cascade and CRISPR
            "214",  // Chimeric Antigen Receptors
            "228",  // Directed Evolution of Enzymes
            "3",    // DNA Polymerase
            "261",  // DNA-Sequencing Nanopores
            "218",  // EPSP Synthase and Weedkillers
            "174",  // GFP-like Proteins
            "77",   // Glucose Oxidase
            "42",   // Green Fluorescent Protein (GFP)
            "199",  // Monellin
            "12",   // Pepsin
            "225",  // Phytase
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
            "89",   // Aconitase and Iron Regulatory Protein 1
            "210",  // Adenine Riboswitch in Action
            "215",  // Albumin
            "97",   // Circadian Clock Proteins
            "4",    // Collagen
            "124",  // Concanavalin A and Circular Permutation
            "127",  // Dengue Virus
            "259",  // Designed Proteins and Citizen Science
            "23",   // DNA
            "262",  // Fifty Years of Open Access to PDB Structures
            "206",  // Globin Evolution
            "41",   // Hemoglobin
            "14",   // Insulin
            "200",  // Zika Virus

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
            "100",  // Adrenergic Receptors
            "251",  // Adenylyl Cyclase
            "203",  // Aminopeptidase 1 and Autophagy
            "21",   // Antibodies
            "173",  // Aquaporin
            "72",   // ATP Synthase
            "27",   // Bacteriorhodopsin
            "181",  // Cascade and CRISPR
            "250",  // Capsaicin Receptor TRPV1
            "97",   // Circadian Clock Proteins
            "276",  // Click Chemistry
            "228",  // Directed Evolution of Enzymes
            "23",   // DNA
            "312",  // FOXP3
            "58",   // G Proteins
            "41",   // Hemoglobin
            "252",  // Hepatitis C Virus Protease/Helicase
            "240",  // Hypoxia-Inducible Factors
            "1",    // Myoglobin
            "142",  // PDB Pioneers
            "22",   // Photosystem I
            "59",   // Photosystem II
            "223",  // Piezo1 Mechanosensitive Channel
            "38",   // Potassium Channels
            "105",  // Ribonuclease A
            "10",   // Ribosomal Subunits
            "121",  // Ribosome
            "40",   // RNA Polymerase
            "245",  // Spliceosomes
            "227",  // Telomerase
            "109",  // Tobacco Mosaic Virus

            "PDB Data",
            "262",  // Fifty Years of Open Access to PDB Structures

            "Protein Structure Prediction",
            "259",  // Designed Proteins and Citizen Science
            "70",   // Designer Proteins
            "287"   // ZAR1 Resistosome

    )
}