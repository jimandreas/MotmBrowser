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

        "Antimicrobial Resistance",
        "146",  // Aminoglycoside Antibiotics
        "226",  // Aminoglycoside Antibiotics and Resistance
        "307",  // Capturing Beta-Lactamase in Action
        "34",   // Dihydrofolate Reductase
        "95",   // Multidrug Resistance Transporters
        "187",  // New Delhi Metallo-Beta-Lactamase
        "29",   // Penicillin-binding Proteins
        "121",  // Ribosome
        "188",  // Tetrahydrobiopterin Biosynthesis
        "192",  // Vancomycin

        "Cancer",

        "160",  // Actinomycin
        "279",  // Anaphase-Promoting Complex / Cyclosome
        "177",  // Apoptosomes
        "284",  // ATM and ATR Kinases
        "283",  // c-Abl Protein Kinase and Imatinib
        "56",   // Caspases
        "214",  // Chimeric Antigen Receptors
        "255",  // Cisplatin and DNA
        "236",  // Cyclin and Cyclin-dependent Kinase
        "34",   // Dihydrofolate Reductase
        "126",  // Epidermal Growth Factor
        "45",   // Estrogen Receptor
        "212",  // Glutathione Transferases
        "252",  // Hepatitis C Virus Protease/Helicase
        "268",  // HER2/neu and Trastuzumab
        "285",  // Histone Deacetylases
        "108",  // Hsp90
        "221",  // Human Papillomavirus and Vaccines
        "230",  // Initiation Factor eIF4E
        "62",   // Major Histocompatibility Complex
        "234",  // MDM2 and Cancer
        "175",  // Microtubules
        "237",  // Nanodiscs and HDL
        "269",  // Nicotine, Cancer, and Addiction
        "271",  // Non-Homologous End Joining Supercomplexes
        "31",   // p53 Tumor Suppressor
        "204",  // PD-1 (Programmed Cell Death Protein 1)
        "270",  // Pyruvate Kinase M2
        "195",  // RAF Protein Kinases
        "148",  // Ras Protein
        "172",  // RecA and Rad51
        "238",  // Ribonucleotide Reductase
        "47",   // Simian Virus 40
        "98",   // Small Interfering RNA (siRNA)
        "245",  // Spliceosomes
        "43",   // Src Tyrosine Kinase
        "227",  // Telomerase
        "91",   // Thymine Dimers
        "73",   // Topoisomerases
        "267",  // VEGF and Angiogenesis

        "Coronavirus",
        "242",  // Coronavirus Proteases
        "249",  // SARS-CoV-2 RNA-dependent RNA Polymerase
        "256",  // SARS-CoV-2 Spike and Antibodies
        "246",  // SARS-CoV-2 Spike
        "258",  // Glucocorticoid Receptor and Dexamethasone
        "264",  // SARS-CoV-2 Spike Variants
        "278",  // SARS-CoV-2 Nucleocapsid and Home Tests

        "Diabetes",
        "194",  // Designer Insulins
        "202",  // Dipeptidyl Peptidase 4
        "311",  // GLP-1 Receptor Agonists
        "184",  // Glucagon
        "77",   // Glucose Oxidase
        "208",  // Glucose Transporters
        "310",  // Incretins
        "14",   // Insulin
        "182",  // Insulin Receptor
        "186",  // Receptor for Advanced Glycation End Products

        "Drug Action",
        "160",  // Actinomycin
        "100",  // Adrenergic Receptors
        "279",  // Anaphase-Promoting Complex / Cyclosome
        "146",  // Aminoglycoside Antibiotics
        "226",  // Aminoglycoside Antibiotics and Resistance
        "115",  // Beta-secretase
        "307",  // Capturing Beta-Lactamase in Action
        "97",   // Circadian Clock Proteins
        "276",  // Click Chemistry
        "17",   // Cyclooxygenase
        "82",   // Cytochrome p450
        "34",   // Dihydrofolate Reductase
        "45",   // Estrogen Receptor
        "311",  // GLP-1 Receptor Agonists
        "258",  // Glucocorticoid Receptor and Dexamethasone
        "191",  // Glutamate-gated Chloride Receptors
        "212",  // Glutathione Transferases
        "285",  // Histone Deacetylases
        "169",  // HIV Envelope Glycoprotein
        "113",  // Influenza Neuraminidase
        "135",  // Integrase
        "128",  // Interferons
        "102",  // Lactate Dehydrogenase
        "175",  // Microtubules
        "95",   // Multidrug Resistance Transporters
        "171",  // Neurotransmitter Transporters
        "187",  // New Delhi Metallo-Beta-Lactamase
        "133",  // Nitric Oxide Synthase
        "217",  // Opioid Receptors
        "123",  // P-glycoprotein
        "29",   // Penicillin-binding Proteins
        "121",  // Ribosome
        "164",  // Serotonin Receptor
        "188",  // Tetrahydrobiopterin Biosynthesis
        "192",  // Vancomycin


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

        "HIV and AIDS",
        "170",  // Broadly Neutralizing Antibodies
        "181",  // Cascade and CRISPR
        "163",  // HIV Capsid
        "169",  // HIV Envelope Glycoprotein
        "33",   // HIV Reverse Transcriptase
        "6",    // HIV-1 Protease
        "135",  // Integrase
        "63",   // T-Cell Receptor

        "Immune System",
        "21",   // Antibodies
        "170",  // Broadly Neutralizing Antibodies
        "214",  // Chimeric Antigen Receptors
        "162",  // Dermcidin
        "178",  // Ebola Virus Proteins
        "312",  // FOXP3
        "76",   // Hemagglutinin
        "113",  // Influenza Neuraminidase
        "128",  // Interferons
        "224",  // Legumain
        "9",    // Lysozyme
        "62",   // Major Histocompatibility Complex
        "231",  // Measles Virus Proteins
        "136",  // Nanobodies
        "204",  // PD-1 (Programmed Cell Death Protein 1)
        "256",  // SARS-CoV-2 Spike and Antibodies
        "193",  // Siderocalin
        "98",   // Small Interfering RNA (siRNA)
        "63",   // T-Cell Receptor
        "209",  // Tissue Transglutaminase and Celiac Disease
        "143",  // Toll-like Receptors
        "272",  // Secretory Antibodies
        "278",  // SARS-CoV-2 Nucleocapsid and Home Tests
        "280",  // MHC I Peptide Loading Complex
        "286",  // RSV Fusion Glycoprotein
        "287",  // ZAR1 Resistosome

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

        "Toxins and Poisons",
        "71",   // Acetylcholine Receptor
        "54",   // Acetylcholinesterase
        "19",   // Actin
        "28",   // Anthrax Toxin
        "173",  // Aquaporin
        "69",   // Cholera Toxin
        "82",   // Cytochrome p450
        "58",   // G Proteins
        "212",  // Glutathione Transferases
        "41",   // Hemoglobin
        "310",  // Incretins
        "196",  // Lead Poisoning
        "239",  // Phospholipase A2
        "38",   // Potassium Channels
        "161",  // Ricin
        "40",   // RNA Polymerase
        "118",  // Sodium-Potassium Pump
        "73",   // Topoisomerases

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
        "256",   // SARS-CoV-2 Spike and Antibodies
        "Viruses",

        "132",  // Adenovirus
        "308",  // Arc
        "2",    // Bacteriophage phiX174
        "170",  // Broadly Neutralizing Antibodies
        "242",  // Coronavirus Proteases
        "103",  // Dengue Virus
        "306",  // Eastern Equine Encephalitis Virus
        "178",  // Ebola Virus Proteins
        "258",  // Glucocorticoid Receptor and Dexamethasone
        "302",  // H5 Hemagglutinin
        "76",   // Hemagglutinin
        "252",  // Hepatitis C Virus Protease/Helicase
        "163",  // HIV Capsid
        "169",  // HIV Envelope Glycoprotein
        "33",   // HIV Reverse Transcriptase
        "221",  // Human Papillomavirus and Vaccines
        "113",  // Influenza Neuraminidase
        "135",  // Integrase
        "231",  // Measles Virus Proteins
        "125",  // Parvoviruses
        "20",   // Poliovirus and Rhinovirus
        "200",  // Quasisymmetry in Icosahedral Viruses
        "286",  // RSV Fusion Glycoprotein
        "278",  // SARS-CoV-2 Nucleocapsid and Home Tests
        "249",  // SARS-CoV-2 RNA-dependent RNA Polymerase
        "256",  // SARS-CoV-2 Spike and Antibodies
        "246",  // SARS-CoV-2 Spike
        "47",   // Simian Virus 40
        "98",   // Small Interfering RNA (siRNA)
        "109",  // Tobacco Mosaic Virus
        "292",  // YES Complex
        "197",  // Zika Virus

        "You and Your Health",
        "156",  // ABO Blood Type Glycosyltransferases
        "54",   // Acetylcholinesterase
        "13",   // Alcohol Dehydrogenase
        "79",   // Amyloid-beta Precursor Protein
        "189",  // Amyloids
        "92",   // Anabolic Steroids
        "298",  // Angiotensin and Blood Pressure
        "177",  // Apoptosomes
        "115",  // Beta-secretase
        "51",   // Calcium Pump
        "49",   // Carbonic Anhydrase
        "66",   // Carotenoid Oxygenase
        "56",   // Caspases
        "57",   // Catalase
        "293",  // CFTR and Cystic Fibrosis
        "97",   // Circadian Clock Proteins
        "4",    // Collagen
        "127",  // Crystallins
        "82",   // Cytochrome p450
        "139",  // DNA Methyltransferases
        "45",   // Estrogen Receptor
        "257",  // Fetal Hemoglobin
        "83",   // Fibrin
        "312",  // FOXP3
        "311",  // GLP-1 Receptor Agonists
        "138",  // Glucansucrase
        "52",   // Growth Hormone
        "41",   // Hemoglobin
        "291",  // Hyaluronidases
        "151",  // Hypoxanthine-guanine phosphoribosyltransferase (HGPRT)
        "240",  // Hypoxia-Inducible Factors
        "14",   // Insulin
        "149",  // Leptin
        "9",    // Lysozyme
        "62",   // Major Histocompatibility Complex
        "68",   // Neurotrophins
        "269",  // Nicotine, Cancer, and Addiction
        "217",  // Opioid Receptors
        "96",   // Oxidosqualene Cyclase
        "12",   // Pepsin
        "61",   // Phenylalanine Hydroxylase
        "239",  // Phospholipase A2
        "223",  // Piezo1 Mechanosensitive Channel
        "101",  // Prions
        "158",  // Proton-Gated Urea Channel
        "233",  // S-Nitrosylated Hemoglobin
        "278",  // SARS-CoV-2 Nucleocapsid and Home Tests
        "104",  // Selenocysteine Synthase
        "164",  // Serotonin Receptor
        "53",   // Serpins
        "37",   // Serum Albumin
        "213",  // Sirtuins
        "94",   // Superoxide Dismutase
        "63",   // T-Cell Receptor
        "188",  // Tetrahydrobiopterin Biosynthesis
        "25",   // Thrombin
        "75",   // Tissue Factor
        "209",  // Tissue Transglutaminase and Celiac Disease
        "155",  // Vitamin D Receptor
        "288",  // Vitamins
        "117",  // Xanthine Oxidoreductase

    )

    val MotmCategoryLife = arrayOf(

        "Section Molecules of Life",


        "Biological Energy",
        "89",   // Aconitase and Iron Regulatory Protein 1
        "13",   // Alcohol Dehydrogenase
        "74",   // Alpha-amylase
        "72",   // ATP Synthase
        "27",   // Bacteriorhodopsin
        "198",  // Beta-galactosidase
        "216",  // Biodegradable Plastic
        "93",   // Citrate Synthase
        "154",  // Citric Acid Cycle
        "144",  // Complex I
        "137",  // Cytochrome bc1
        "36",   // Cytochrome c
        "5",    // Cytochrome c Oxidase
        "90",   // Fatty Acid Synthase
        "174",  // GFP-like Proteins
        "24",   // Glycogen Phosphorylase
        "50",   // Glycolytic Enzymes
        "42",   // Green Fluorescent Protein (GFP)
        "240",  // Hypoxia-Inducible Factors
        "129",  // Isocitrate Dehydrogenase
        "102",  // Lactate Dehydrogenase
        "78",   // Luciferase
        "1",    // Myoglobin
        "244",  // Photosynthetic Supercomplexes
        "22",   // Photosystem I
        "183",  // Phototropin
        "153",  // Pyruvate Dehydrogenase Complex
        "147",  // Rhodopsin
        "46",   // Trypsin
        "270",  // Pyruvate Kinase M2
        "273",  // Respiratory Supercomplex
        "281",  // Cellulases and Bioenergy

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

        "Bioluminescence and Fluorescence",
        "229",  // Fluorescent RNA Aptamers
        "174",  // GFP-like Proteins
        "42",   // Green Fluorescent Protein (GFP)

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

        "Enzymes",
        "80",   // AAA+ Proteases
        "156",  // ABO Blood Type Glycosyltransferases
        "54",   // Acetylcholinesterase
        "89",   // Aconitase and Iron Regulatory Protein 1
        "13",   // Alcohol Dehydrogenase
        "74",   // Alpha-amylase
        "16",   // Aminoacyl-tRNA Synthetases
        "92",   // Anabolic Steroids
        "215",  // Aspartate Transcarbamoylase
        "198",  // Beta-galactosidase
        "115",  // Beta-secretase
        "152",  // cAMP-dependent Protein Kinase (PKA)
        "49",   // Carbonic Anhydrase
        "66",   // Carotenoid Oxygenase
        "56",   // Caspases
        "57",   // Catalase
        "93",   // Citrate Synthase
        "154",  // Citric Acid Cycle
        "17",   // Cyclooxygenase
        "82",   // Cytochrome p450
        "220",  // Dehalogenases
        "34",   // Dihydrofolate Reductase
        "55",   // DNA Ligase
        "218",  // EPSP Synthase and Weedkillers
        "86",   // Exosomes
        "90",   // Fatty Acid Synthase
        "138",  // Glucansucrase
        "77",   // Glucose Oxidase
        "30",   // Glutamine Synthetase
        "24",   // Glycogen Phosphorylase
        "50",   // Glycolytic Enzymes
        "33",   // HIV Reverse Transcriptase
        "111",  // Hydrogenase
        "151",  // Hypoxanthine-guanine phosphoribosyltransferase (HGPRT)
        "129",  // Isocitrate Dehydrogenase
        "102",  // Lactate Dehydrogenase
        "224",  // Legumain
        "78",   // Luciferase
        "9",    // Lysozyme
        "179",  // Methyl-coenzyme M Reductase
        "187",  // New Delhi Metallo-Beta-Lactamase
        "26",   // Nitrogenase
        "141",  // O-GlcNAc Transferase
        "96",   // Oxidosqualene Cyclase
        "12",   // Pepsin
        "61",   // Phenylalanine Hydroxylase
        "225",  // Phytase
        "106",  // Poly(A) Polymerase
        "153",  // Pyruvate Dehydrogenase Complex
        "8",    // Restriction Enzymes
        "140",  // Rhomboid Protease GlpG
        "105",  // Ribonuclease A
        "238",  // Ribonucleotide Reductase
        "121",  // Ribosome
        "40",   // RNA Polymerase
        "11",   // Rubisco
        "104",  // Selenocysteine Synthase
        "65",   // Self-splicing RNA
        "43",   // Src Tyrosine Kinase
        "116",  // Sulfotransferases
        "94",   // Superoxide Dismutase
        "188",  // Tetrahydrobiopterin Biosynthesis
        "25",   // Thrombin
        "91",   // Thymine Dimers
        "209",  // Tissue Transglutaminase and Celiac Disease
        "73",   // Topoisomerases
        "84",   // Transposase
        "46",   // Trypsin
        "117",  // Xanthine Oxidoreductase
        "263",  // Acetohydroxyacid Synthase
        "277",  // Plastic-eating Enzymes
        "301",  // Assembly Line Polyketide Synthases

        "Molecular Evolution",
        "120",  // Antifreeze Proteins
        "308",  // Arc
        "124",  // Concanavalin A and Circular Permutation
        "36",   // Cytochrome c
        "5",    // Cytochrome c Oxidase
        "34",   // Dihydrofolate Reductase
        "228",  // Directed Evolution of Enzymes
        "206",  // Globin Evolution
        "78",   // Luciferase
        "125",  // Parvoviruses
        "29",   // Penicillin-binding Proteins
        "121",  // Ribosome
        "295",  // Ribosome Diversity
        "84",   // Transposase
        "192",  // Vancomycin
        "155",  // Vitamin D Receptor


        "Molecular Infrastructure",
        "19",   // Actin
        "99",   // Cadherin
        "254",  // Cellulose Synthase
        "4",    // Collagen
        "134",  // Integrin
        "175",  // Microtubules
        "211",  // Pilus Machine
        "232",  // Proteins and Biominerals
        "209",  // Tissue Transglutaminase and Celiac Disease
        "185",  // Titin
        "114",  // Vaults
        "275",  // Actin Branching by Arp2/3 Complex
        "291",  // Hyaluronidases
        "296",  // ESCRT-III

        "Molecular Motors",
        "72",   // ATP Synthase
        "176",  // Dynein
        "64",   // Kinesin
        "18",   // Myosin
        "211",  // Pilus Machine
        "219",  // Vacuolar ATPase
        "300",  // Flagellar Motor
        "305",  // TOC-TIC Translocon

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
        "277",   // Plastic-eating Enzymes

        "Nucleic Acids",
        "210",  // Adenine Riboswitch in Action
        "284",  // ATM and ATR Kinases
        "119",  // Designed DNA Crystal
        "23",   // DNA
        "229",  // Fluorescent RNA Aptamers
        "312",  // FOXP3
        "313",  // Natural RNA-Only Assemblies
        "7",    // Nucleosome
        "172",  // RecA and Rad51
        "8",    // Restriction Enzymes
        "260",  // Ribonuclease P
        "10",   // Ribosomal Subunits
        "121",  // Ribosome
        "130",  // Riboswitches
        "65",   // Self-splicing RNA
        "98",   // Small Interfering RNA (siRNA)
        "245",  // Spliceosomes
        "227",  // Telomerase
        "91",   // Thymine Dimers
        "15",   // Transfer RNA
        "157",  // Transfer-Messenger RNA
        "155",  // Vitamin D Receptor

        "Protein Synthesis",
        "80",   // AAA+ Proteases
        "89",   // Aconitase and Iron Regulatory Protein 1
        "210",  // Adenine Riboswitch in Action
        "16",   // Aminoacyl-tRNA Synthetases
        "181",  // Cascade and CRISPR
        "48",   // Catabolite Activator Protein
        "32",   // Chaperones
        "23",   // DNA
        "168",  // DNA Helicase
        "55",   // DNA Ligase
        "139",  // DNA Methyltransferases
        "3",    // DNA Polymerase
        "81",   // Elongation Factors
        "122",  // Enhanceosome
        "45",   // Estrogen Receptor
        "86",   // Exosomes
        "253",  // Expressome
        "41",   // Hemoglobin
        "33",   // HIV Reverse Transcriptase
        "108",  // Hsp90
        "230",  // Initiation Factor eIF4E
        "131",  // Inteins
        "39",   // lac Repressor
        "9",    // Lysozyme
        "289",  // Mediator
        "145",  // Messenger RNA Capping
        "7",    // Nucleosome
        "141",  // O-GlcNAc Transferase
        "112",  // Oct and Sox Transcription Factors
        "266",  // Oligosaccharyltransferase
        "31",   // p53 Tumor Suppressor
        "106",  // Poly(A) Polymerase
        "166",  // Proteasome
        "172",  // RecA and Rad51
        "8",    // Restriction Enzymes
        "140",  // Rhomboid Protease GlpG
        "260",  // Ribonuclease P
        "10",   // Ribosomal Subunits
        "121",  // Ribosome
        "295",  // Ribosome Diversity
        "130",  // Riboswitches
        "40",   // RNA Polymerase
        "104",  // Selenocysteine Synthase
        "65",   // Self-splicing RNA
        "213",  // Sirtuins
        "150",  // Sliding Clamps
        "98",   // Small Interfering RNA (siRNA)
        "67",   // TATA-Binding Protein
        "91",   // Thymine Dimers
        "73",   // Topoisomerases
        "15",   // Transfer RNA
        "157",  // Transfer-Messenger RNA
        "84",   // Transposase
        "60",   // Ubiquitin
        "87",   // Zinc Fingers

        "Transport",
        "203",  // Aminopeptidase 1 and Autophagy
        "92",   // Anabolic Steroids
        "173",  // Aquaporin
        "293",  // CFTR and Cystic Fibrosis
        "88",   // Clathrin
        "159",  // Erythrocruorin
        "35",   // Ferritin and Transferrin
        "257",  // Fetal Hemoglobin
        "265",  // Golgi Casein Kinase
        "208",  // Glucose Transporters
        "41",   // Hemoglobin
        "85",   // Importins
        "294",  // Injectisome
        "95",   // Multidrug Resistance Transporters
        "237",  // Nanodiscs and HDL
        "290",  // Nanowires
        "171",  // Neurotransmitter Transporters
        "205",  // Nuclear Pore Complex
        "123",  // P-glycoprotein
        "158",  // Proton-Gated Urea Channel
        "233",  // S-Nitrosylated Hemoglobin
        "37",   // Serum Albumin
        "118",  // Sodium-Potassium Pump
        "305"  // TOC-TIC Translocon
    )

    val MotmCategoryBiotech = arrayOf(

        "Section Biotech and Nanotech",


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
        "132",  // Adenovirus
        "21",   // Antibodies
        "301",  // Assembly Line Polyketide Synthases
        "181",  // Cascade and CRISPR
        "124",  // Concanavalin A and Circular Permutation
        "137",  // Cytochrome bc1
        "119",  // Designed DNA Crystal
        "165",  // Designed Protein Cages
        "70",   // Designer Proteins
        "261",  // DNA-Sequencing Nanopores
        "229",  // Fluorescent RNA Aptamers
        "174",  // GFP-like Proteins
        "42",   // Green Fluorescent Protein (GFP)
        "131",  // Inteins
        "237",  // Nanodiscs and HDL
        "290",  // Nanowires
        "232",  // Proteins and Biominerals
        "222",  // Proteins and Nanoparticles
        "161",  // Ricin
        "180",  // TAL Effectors

        "Recombinant DNA",
        "198",  // Beta-galactosidase
        "55",   // DNA Ligase
        "3",    // DNA Polymerase
        "112",  // Oct and Sox Transcription Factors
        "8",    // Restriction Enzymes


        "Renewable Energy",
        "281",   // Cellulases and Bioenergy
        "228",  // Directed Evolution of Enzymes
        "111",  // Hydrogenase
        "179"  // Methyl-coenzyme M Reductase
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