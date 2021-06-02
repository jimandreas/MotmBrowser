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

@file:Suppress("ReplaceCallWithBinaryOperator", "UnnecessaryVariable", "UnnecessaryVariable")

package com.bammellab.mollib.data

import java.util.*

object PdbInfo {

    data class PdbEntryInfo(val pdbName: String, val pdbInfo: String)

    /**
     * iterate through the fruits and a list of entries
     * where the name contains the search string
     */
    fun searchPdbInfo(searchTerm: String): List<PdbEntryInfo> {
        //val startTime = System.currentTimeMillis()
        val s = searchTerm.lowercase(Locale.ROOT)
        val match = pdbInfoList.filter {
            it.pdbInfo.lowercase(Locale.ROOT).contains(s)
                    || it.pdbName.lowercase(Locale.ROOT).equals(s)
        }
//        Timber.v("TIME LAPSED: %d milliseoncs, %d matches",
//                System.currentTimeMillis() - startTime,
//                 match.size)
        return match
    }

    fun searchPdbInfoForNameMatch(searchTerm: String): List<PdbEntryInfo> {
        //val startTime = System.currentTimeMillis()
        val s = searchTerm.lowercase(Locale.ROOT)
        val match = pdbInfoList.filter {
            it.pdbName.lowercase(Locale.ROOT).equals(s)
        }
//        Timber.v("TIME LAPSED: %d milliseoncs, %d matches",
//                System.currentTimeMillis() - startTime,
//                 match.size)
        return match
    }

    fun obtainPdbInfoList(): List<PdbEntryInfo> {
        return pdbInfoList
    }

    private val pdbInfoList = listOf(
        PdbEntryInfo(
            "143d",
            "SOLUTION STRUCTURE OF THE HUMAN TELOMERIC REPEAT D(AG3[T2AG3]3) OF THE G-QUADRUPLEX"
        ),
        PdbEntryInfo(
            "148l",
            "A COVALENT ENZYME-SUBSTRATE INTERMEDIATE WITH SACCHARIDE DISTORTION IN A MUTANT T4 LYSOZYME"
        ),
        PdbEntryInfo(
            "173d",
            "MULTIPLE BINDING MODES OF ANTICANCER DRUG ACTINOMYCIN D: X-RAY, MOLECULAR MODELING, AND SPECTROSCOPIC STUDIES OF D(GAAGCTTC)2-ACTINOMYCIN D COMPLEXES AND ITS HOST DNA"
        ),
        PdbEntryInfo(
            "1a0h",
            "THE X-RAY CRYSTAL STRUCTURE OF PPACK-MEIZOTHROMBIN DESF1: KRINGLE/THROMBIN AND CARBOHYDRATE/KRINGLE/THROMBIN INTERACTIONS AND LOCATION OF THE LINKER CHAIN"
        ),
        PdbEntryInfo("1a0i", "ATP-DEPENDENT DNA LIGASE FROM BACTERIOPHAGE T7 COMPLEX WITH ATP"),
        PdbEntryInfo(
            "1a1t",
            "STRUCTURE OF THE HIV-1 NUCLEOCAPSID PROTEIN BOUND TO THE SL3 PSI-RNA RECOGNITION ELEMENT, NMR, 25 STRUCTURES"
        ),
        PdbEntryInfo(
            "1a31",
            "HUMAN RECONSTITUTED DNA TOPOISOMERASE I IN COVALENT COMPLEX WITH A 22 BASE PAIR DNA DUPLEX"
        ),
        PdbEntryInfo("1a36", "TOPOISOMERASE I/DNA COMPLEX"),
        PdbEntryInfo(
            "1a3w",
            "PYRUVATE KINASE FROM SACCHAROMYCES CEREVISIAE COMPLEXED WITH FBP, PG, MN2+ AND K+"
        ),
        PdbEntryInfo(
            "1a52",
            "ESTROGEN RECEPTOR ALPHA LIGAND-BINDING DOMAIN COMPLEXED TO ESTRADIOL"
        ),
        PdbEntryInfo("1a59", "COLD-ACTIVE CITRATE SYNTHASE"),
        PdbEntryInfo("1a9w", "HUMAN EMBRYONIC GOWER II CARBONMONOXY HEMOGLOBIN"),
        PdbEntryInfo("1acc", "ANTHRAX PROTECTIVE ANTIGEN"),
        PdbEntryInfo(
            "1acj",
            "QUATERNARY LIGAND BINDING TO AROMATIC RESIDUES IN THE ACTIVE-SITE GORGE OF ACETYLCHOLINESTERASE"
        ),
        PdbEntryInfo(
            "1adc",
            "CRYSTALLOGRAPHIC STUDIES OF ISOSTERIC NAD ANALOGUES BOUND TO ALCOHOL DEHYDROGENASE: SPECIFICITY AND SUBSTRATE BINDING IN TWO TERNARY COMPLEXES"
        ),
        PdbEntryInfo("1aew", "L-CHAIN HORSE APOFERRITIN"),
        PdbEntryInfo("1agn", "X-RAY STRUCTURE OF HUMAN SIGMA ALCOHOL DEHYDROGENASE"),
        PdbEntryInfo(
            "1ajk",
            "CIRCULARLY PERMUTED (1-3,1-4)-BETA-D-GLUCAN 4-GLUCANOHYDROLASE CPA16M-84"
        ),
        PdbEntryInfo(
            "1ajo",
            "CIRCULARLY PERMUTED (1-3,1-4)-BETA-D-GLUCAN 4-GLUCANOHYDROLASE CPA16M-127"
        ),
        PdbEntryInfo(
            "1ak4",
            "HUMAN CYCLOPHILIN A BOUND TO THE AMINO-TERMINAL DOMAIN OF HIV-1 CAPSID"
        ),
        PdbEntryInfo(
            "1akj",
            "COMPLEX OF THE HUMAN MHC CLASS I GLYCOPROTEIN HLA-A2 AND THE T CELL CORECEPTOR CD8"
        ),
        PdbEntryInfo("1am1", "ATP BINDING SITE IN THE HSP90 MOLECULAR CHAPERONE"),
        PdbEntryInfo("1am2", "GYRA INTEIN FROM MYCOBACTERIUM XENOPI"),
        PdbEntryInfo("1ana", "HELIX GEOMETRY AND HYDRATION IN AN A-DNA TETRAMER. IC-C-G-G"),
        PdbEntryInfo(
            "1aoi",
            "COMPLEX BETWEEN NUCLEOSOME CORE PARTICLE (H3,H4,H2A,H2B) AND 146 BP LONG DNA FRAGMENT"
        ),
        PdbEntryInfo(
            "1aon",
            "CRYSTAL STRUCTURE OF THE ASYMMETRIC CHAPERONIN COMPLEX GROEL/GROES/(ADP)7"
        ),
        PdbEntryInfo(
            "1ap8",
            "TRANSLATION INITIATION FACTOR EIF4E IN COMPLEX WITH M7GDP, NMR, 20 STRUCTURES"
        ),
        PdbEntryInfo(
            "1aqu",
            "ESTROGEN SULFOTRANSFERASE WITH BOUND INACTIVE COFACTOR PAP AND 17-BETA ESTRADIOL"
        ),
        PdbEntryInfo(
            "1asz",
            "THE ACTIVE SITE OF YEAST ASPARTYL-TRNA SYNTHETASE: STRUCTURAL AND FUNCTIONAL ASPECTS OF THE AMINOACYLATION REACTION"
        ),
        PdbEntryInfo("1atn", "Atomic structure of the actin:DNASE I complex"),
        PdbEntryInfo(
            "1atp",
            "2.2 angstrom refined crystal structure of the catalytic subunit of cAMP-dependent protein kinase complexed with MNATP and a peptide inhibitor"
        ),
        PdbEntryInfo("1au1", "HUMAN INTERFERON-BETA CRYSTAL STRUCTURE"),
        PdbEntryInfo("1ax8", "Human obesity protein, leptin"),
        PdbEntryInfo(
            "1b41",
            "HUMAN ACETYLCHOLINESTERASE COMPLEXED WITH FASCICULIN-II, GLYCOSYLATED PROTEIN"
        ),
        PdbEntryInfo(
            "1b5s",
            "DIHYDROLIPOYL TRANSACETYLASE (E.C.2.3.1.12) CATALYTIC DOMAIN (RESIDUES 184-425) FROM BACILLUS STEAROTHERMOPHILUS"
        ),
        PdbEntryInfo("1b7t", "MYOSIN DIGESTED BY PAPAIN"),
        PdbEntryInfo("1b89", "CLATHRIN HEAVY CHAIN PROXIMAL LEG SEGMENT (BOVINE)"),
        PdbEntryInfo("1b98", "NEUROTROPHIN 4 (HOMODIMER)"),
        PdbEntryInfo(
            "1bbl",
            "THREE-DIMENSIONAL SOLUTION STRUCTURE OF THE E3-BINDING DOMAIN OF THE DIHYDROLIPOAMIDE SUCCINYLTRANSFERASE CORE FROM THE 2-OXOGLUTARATE DEHYDROGENASE MULTIENZYME COMPLEX OF ESCHERICHIA COLI"
        ),
        PdbEntryInfo(
            "1bbt",
            "METHODS USED IN THE STRUCTURE DETERMINATION OF FOOT AND MOUTH DISEASE VIRUS"
        ),
        PdbEntryInfo(
            "1bd2",
            "COMPLEX BETWEEN HUMAN T-CELL RECEPTOR B7, VIRAL PEPTIDE (TAX) AND MHC CLASS I MOLECULE HLA-A 0201"
        ),
        PdbEntryInfo("1bdg", "HEXOKINASE FROM SCHISTOSOMA MANSONI COMPLEXED WITH GLUCOSE"),
        PdbEntryInfo(
            "1bet",
            "NEW PROTEIN FOLD REVEALED BY A 2.3 ANGSTROM RESOLUTION CRYSTAL STRUCTURE OF NERVE GROWTH FACTOR"
        ),
        PdbEntryInfo("1bg2", "HUMAN UBIQUITOUS KINESIN MOTOR DOMAIN"),
        PdbEntryInfo("1bgw", "TOPOISOMERASE RESIDUES 410-1202,"),
        PdbEntryInfo(
            "1bi7",
            "MECHANISM OF G1 CYCLIN DEPENDENT KINASE INHIBITION FROM THE STRUCTURE OF THE CDK6-P16INK4A TUMOR SUPPRESSOR COMPLEX"
        ),
        PdbEntryInfo("1bkd", "COMPLEX OF HUMAN H-RAS WITH HUMAN SOS-1"),
        PdbEntryInfo("1bkv", "COLLAGEN"),
        PdbEntryInfo("1bl8", "POTASSIUM CHANNEL (KCSA) FROM STREPTOMYCES LIVIDANS"),
        PdbEntryInfo(
            "1blb",
            "CLOSE PACKING OF AN OLIGOMERIC EYE LENS BETA-CRYSTALLIN INDUCES LOSS OF SYMMETRY AND ORDERING OF SEQUENCE EXTENSIONS"
        ),
        PdbEntryInfo("1bln", "ANTI-P-GLYCOPROTEIN FAB MRK-16"),
        PdbEntryInfo("1bna", "STRUCTURE OF A B-DNA DODECAMER. CONFORMATION AND DYNAMICS"),
        PdbEntryInfo(
            "1bo4",
            "CRYSTAL STRUCTURE OF A GCN5-RELATED N-ACETYLTRANSFERASE: SERRATIA MARESCENS AMINOGLYCOSIDE 3-N-ACETYLTRANSFERASE"
        ),
        PdbEntryInfo("1boy", "EXTRACELLULAR REGION OF HUMAN TISSUE FACTOR"),
        PdbEntryInfo(
            "1bp2",
            "STRUCTURE OF BOVINE PANCREATIC PHOSPHOLIPASE A2 AT 1.7 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo("1bpo", "CLATHRIN HEAVY-CHAIN TERMINAL DOMAIN AND LINKER"),
        PdbEntryInfo("1br0", "THREE DIMENSIONAL STRUCTURE OF THE N-TERMINAL DOMAIN OF SYNTAXIN 1A"),
        PdbEntryInfo(
            "1br1",
            "SMOOTH MUSCLE MYOSIN MOTOR DOMAIN-ESSENTIAL LIGHT CHAIN COMPLEX WITH MGADP.ALF4 BOUND AT THE ACTIVE SITE"
        ),
        PdbEntryInfo(
            "1brl",
            "THREE-DIMENSIONAL STRUCTURE OF BACTERIAL LUCIFERASE FROM VIBRIO HARVEYI AT 2.4 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo("1buw", "CRYSTAL STRUCTURE OF S-NITROSO-NITROSYL HUMAN HEMOGLOBIN A"),
        PdbEntryInfo(
            "1bx2",
            "CRYSTAL STRUCTURE OF HLA-DR2 (DRA*0101,DRB1*1501) COMPLEXED WITH A PEPTIDE FROM HUMAN MYELIN BASIC PROTEIN"
        ),
        PdbEntryInfo(
            "1bx6",
            "CRYSTAL STRUCTURE OF THE POTENT NATURAL PRODUCT INHIBITOR BALANOL IN COMPLEX WITH THE CATALYTIC SUBUNIT OF CAMP-DEPENDENT PROTEIN KINASE"
        ),
        PdbEntryInfo("1bzy", "HUMAN HGPRTASE WITH TRANSITION STATE INHIBITOR"),
        PdbEntryInfo("1c17", "A1C12 SUBCOMPLEX OF F1FO ATP SYNTHASE"),
        PdbEntryInfo(
            "1c1e",
            "CRYSTAL STRUCTURE OF A DIELS-ALDERASE CATALYTIC ANTIBODY 1E9 IN COMPLEX WITH ITS HAPTEN"
        ),
        PdbEntryInfo("1c3w", "BACTERIORHODOPSIN/LIPID COMPLEX AT 1.55 A RESOLUTION"),
        PdbEntryInfo("1c4e", "GURMARIN FROM GYMNEMA SYLVESTRE"),
        PdbEntryInfo("1c7d", "DEOXY RHB1.2 (RECOMBINANT HEMOGLOBIN)"),
        PdbEntryInfo(
            "1c8m",
            "REFINED CRYSTAL STRUCTURE OF HUMAN RHINOVIRUS 16 COMPLEXED WITH VP63843 (PLECONARIL), AN ANTI-PICORNAVIRAL DRUG CURRENTLY IN CLINICAL TRIALS"
        ),
        PdbEntryInfo("1c96", "S642A:CITRATE COMPLEX OF ACONITASE"),
        PdbEntryInfo("1c9f", "NMR STRUCTURE OF THE CAD DOMAIN OF CASPASE-ACTIVATED DNASE"),
        PdbEntryInfo(
            "1ca2",
            "REFINED STRUCTURE OF HUMAN CARBONIC ANHYDRASE II AT 2.0 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "1cag",
            "CRYSTAL AND MOLECULAR STRUCTURE OF A COLLAGEN-LIKE PEPTIDE AT 1.9 ANGSTROM RESOLUTION"
        ),
        PdbEntryInfo(
            "1cam",
            "STRUCTURAL ANALYSIS OF THE ZINC HYDROXIDE-THR 199-GLU 106 HYDROGEN BONDING NETWORK IN HUMAN CARBONIC ANHYDRASE II"
        ),
        PdbEntryInfo("1cd3", "PROCAPSID OF BACTERIOPHAGE PHIX174"),
        PdbEntryInfo("1cdw", "HUMAN TBP CORE DOMAIN COMPLEXED WITH DNA"),
        PdbEntryInfo(
            "1cet",
            "CHLOROQUINE BINDS IN THE COFACTOR BINDING SITE OF PLASMODIUM FALCIPARUM LACTATE DEHYDROGENASE."
        ),
        PdbEntryInfo("1cfd", "CALCIUM-FREE CALMODULIN"),
        PdbEntryInfo(
            "1cfj",
            "METHYLPHOSPHONYLATED ACETYLCHOLINESTERASE (AGED) OBTAINED BY REACTION WITH O-ISOPROPYLMETHYLPHOSPHONOFLUORIDATE (GB, SARIN)"
        ),
        PdbEntryInfo(
            "1cgp",
            "CATABOLITE GENE ACTIVATOR PROTEIN (CAP)/DNA COMPLEX + ADENOSINE-3',5'-CYCLIC-MONOPHOSPHATE"
        ),
        PdbEntryInfo("1cjb", "MALARIAL PURINE PHOSPHORIBOSYLTRANSFERASE"),
        PdbEntryInfo("1cjw", "SEROTONIN N-ACETYLTRANSFERASE COMPLEXED WITH A BISUBSTRATE ANALOG"),
        PdbEntryInfo("1cjy", "HUMAN CYTOSOLIC PHOSPHOLIPASE A2"),
        PdbEntryInfo(
            "1ckm",
            "STRUCTURE OF TWO DIFFERENT CONFORMATIONS OF MRNA CAPPING ENZYME IN COMPLEX WITH GTP"
        ),
        PdbEntryInfo("1ckn", "STRUCTURE OF GUANYLYLATED MRNA CAPPING ENZYME COMPLEXED WITH GTP"),
        PdbEntryInfo(
            "1cko",
            "STRUCTURE OF MRNA CAPPING ENZYME IN COMPLEX WITH THE CAP ANALOG GPPPG"
        ),
        PdbEntryInfo("1cll", "CALMODULIN STRUCTURE REFINED AT 1.7 ANGSTROMS RESOLUTION"),
        PdbEntryInfo(
            "1clq",
            "CRYSTAL STRUCTURE OF A REPLICATION FORK DNA POLYMERASE EDITING COMPLEX AT 2.7 A RESOLUTION"
        ),
        PdbEntryInfo("1cm1", "MOTIONS OF CALMODULIN-SINGLE-CONFORMER REFINEMENT"),
        PdbEntryInfo(
            "1cnw",
            "SECONDARY INTERACTIONS SIGNIFICANTLY REMOVED FROM THE SULFONAMIDE BINDING POCKET OF CARBONIC ANHYDRASE II INFLUENCE BINDING CONSTANTS"
        ),
        PdbEntryInfo(
            "1cos",
            "CRYSTAL STRUCTURE OF A SYNTHETIC TRIPLE-STRANDED ALPHA-HELICAL BUNDLE"
        ),
        PdbEntryInfo(
            "1cpm",
            "NATIVE-LIKE IN VIVO FOLDING OF A CIRCULARLY PERMUTED JELLYROLL PROTEIN SHOWN BY CRYSTAL STRUCTURE ANALYSIS"
        ),
        PdbEntryInfo(
            "1cq1",
            "Soluble Quinoprotein Glucose Dehydrogenase from Acinetobacter Calcoaceticus in Complex with PQQH2 and Glucose"
        ),
        PdbEntryInfo(
            "1cqi",
            "Crystal Structure of the Complex of ADP and MG2+ with Dephosphorylated E. Coli Succinyl-CoA Synthetase"
        ),
        PdbEntryInfo(
            "1cts",
            "CRYSTALLOGRAPHIC REFINEMENT AND ATOMIC MODELS OF TWO DIFFERENT FORMS OF CITRATE SYNTHASE AT 2.7 AND 1.7 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "1cul",
            "COMPLEX OF GS-ALPHA WITH THE CATALYTIC DOMAINS OF MAMMALIAN ADENYLYL CYCLASE: COMPLEX WITH 2',5'-DIDEOXY-ADENOSINE 3'-TRIPHOSPHATE AND MG"
        ),
        PdbEntryInfo(
            "1cvj",
            "X-RAY CRYSTAL STRUCTURE OF THE POLY(A)-BINDING PROTEIN IN COMPLEX WITH POLYADENYLATE RNA"
        ),
        PdbEntryInfo("1cvn", "CONCANAVALIN A COMPLEXED TO TRIMANNOSIDE"),
        PdbEntryInfo("1cx8", "CRYSTAL STRUCTURE OF THE ECTODOMAIN OF HUMAN TRANSFERRIN RECEPTOR"),
        PdbEntryInfo("1cyo", "BOVINE CYTOCHROME B(5)"),
        PdbEntryInfo(
            "1d09",
            "ASPARTATE TRANSCARBAMOYLASE COMPLEXED WITH N-PHOSPHONACETYL-L-ASPARTATE (PALA)"
        ),
        PdbEntryInfo(
            "1d0r",
            "SOLUTION STRUCTURE OF GLUCAGON-LIKE PEPTIDE-1-(7-36)-AMIDE IN TRIFLUOROETHANOL/WATER"
        ),
        PdbEntryInfo("1d2n", "D2 DOMAIN OF N-ETHYLMALEIMIDE-SENSITIVE FUSION PROTEIN"),
        PdbEntryInfo(
            "1d2s",
            "CRYSTAL STRUCTURE OF THE N-TERMINAL LAMININ G-LIKE DOMAIN OF SHBG IN COMPLEX WITH DIHYDROTESTOSTERONE"
        ),
        PdbEntryInfo(
            "1d6n",
            "TERNARY COMPLEX STRUCTURE OF HUMAN HGPRTASE, PRPP, MG2+, AND THE INHIBITOR HPP REVEALS THE INVOLVEMENT OF THE FLEXIBLE LOOP IN SUBSTRATE BINDING"
        ),
        PdbEntryInfo(
            "1dan",
            "Complex of active site inhibited human blood coagulation factor VIIA with human recombinant soluble tissue factor"
        ),
        PdbEntryInfo("1dar", "ELONGATION FACTOR G IN COMPLEX WITH GDP"),
        PdbEntryInfo(
            "1db1",
            "CRYSTAL STRUCTURE OF THE NUCLEAR RECEPTOR FOR VITAMIN D COMPLEXED TO VITAMIN D"
        ),
        PdbEntryInfo(
            "1dcp",
            "DCOH, A BIFUNCTIONAL PROTEIN-BINDING TRANSCRIPTIONAL COACTIVATOR, COMPLEXED WITH BIOPTERIN"
        ),
        PdbEntryInfo(
            "1ddz",
            "X-RAY STRUCTURE OF A BETA-CARBONIC ANHYDRASE FROM THE RED ALGA, PORPHYRIDIUM PURPUREUM R-1"
        ),
        PdbEntryInfo(
            "1dfg",
            "X-RAY STRUCTURE OF ESCHERICHIA COLI ENOYL REDUCTASE WITH BOUND NAD AND BENZO-DIAZABORINE"
        ),
        PdbEntryInfo("1dfj", "RIBONUCLEASE INHIBITOR COMPLEXED WITH RIBONUCLEASE A"),
        PdbEntryInfo(
            "1dfn",
            "CRYSTAL STRUCTURE OF DEFENSIN HNP-3, AN AMPHIPHILIC DIMER: MECHANISMS OF MEMBRANE PERMEABILIZATION"
        ),
        PdbEntryInfo(
            "1dgi",
            "Cryo-EM structure of human poliovirus(serotype 1)complexed with three domain CD155"
        ),
        PdbEntryInfo(
            "1dgk",
            "MUTANT MONOMER OF RECOMBINANT HUMAN HEXOKINASE TYPE I WITH GLUCOSE AND ADP IN THE ACTIVE SITE"
        ),
        PdbEntryInfo("1dgs", "CRYSTAL STRUCTURE OF NAD+-DEPENDENT DNA LIGASE FROM T. FILIFORMIS"),
        PdbEntryInfo("1dhr", "CRYSTAL STRUCTURE OF RAT LIVER DIHYDROPTERIDINE REDUCTASE"),
        PdbEntryInfo(
            "1dkf",
            "CRYSTAL STRUCTURE OF A HETERODIMERIC COMPLEX OF RAR AND RXR LIGAND-BINDING DOMAINS"
        ),
        PdbEntryInfo(
            "1dkg",
            "CRYSTAL STRUCTURE OF THE NUCLEOTIDE EXCHANGE FACTOR GRPE BOUND TO THE ATPASE DOMAIN OF THE MOLECULAR CHAPERONE DNAK"
        ),
        PdbEntryInfo(
            "1dkq",
            "CRYSTAL STRUCTURE OF PHYTATE COMPLEX ESCHERICHIA COLI PHYTASE AT PH 5.0. PHYTATE IS BOUND WITH ITS 3-PHOSPHATE IN THE ACTIVE SITE. HG2+ CATION ACTS AS AN INTERMOLECULAR BRIDGE"
        ),
        PdbEntryInfo(
            "1dkz",
            "THE SUBSTRATE BINDING DOMAIN OF DNAK IN COMPLEX WITH A SUBSTRATE PEPTIDE, DETERMINED FROM TYPE 1 NATIVE CRYSTALS"
        ),
        PdbEntryInfo(
            "1dlh",
            "CRYSTAL STRUCTURE OF THE HUMAN CLASS II MHC PROTEIN HLA-DR1 COMPLEXED WITH AN INFLUENZA VIRUS PEPTIDE"
        ),
        PdbEntryInfo(
            "1dls",
            "METHOTREXATE-RESISTANT VARIANTS OF HUMAN DIHYDROFOLATE REDUCTASE WITH SUBSTITUTION OF LEUCINE 22: KINETICS, CRYSTALLOGRAPHY AND POTENTIAL AS SELECTABLE MARKERS"
        ),
        PdbEntryInfo(
            "1dmw",
            "CRYSTAL STRUCTURE OF DOUBLE TRUNCATED HUMAN PHENYLALANINE HYDROXYLASE WITH BOUND 7,8-DIHYDRO-L-BIOPTERIN"
        ),
        PdbEntryInfo(
            "1dog",
            "REFINED STRUCTURE FOR THE COMPLEX OF 1-DEOXYNOJIRIMYCIN WITH GLUCOAMYLASE FROM (ASPERGILLUS AWAMORI) VAR. X100 TO 2.4 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "1dsy",
            "C2 DOMAIN FROM PROTEIN KINASE C (ALPHA) COMPLEXED WITH CA2+ AND PHOSPHATIDYLSERINE"
        ),
        PdbEntryInfo(
            "1dze",
            "Structure of the M Intermediate of Bacteriorhodopsin trapped at 100K"
        ),
        PdbEntryInfo(
            "1e08",
            "Structural model of the [Fe]-Hydrogenase/cytochrome c553 complex combining NMR and soft-docking"
        ),
        PdbEntryInfo("1e0u", "Structure R271L mutant of E. coli pyruvate kinase"),
        PdbEntryInfo("1e12", "Halorhodopsin, a light-driven chloride pump"),
        PdbEntryInfo("1e2o", "CATALYTIC DOMAIN FROM DIHYDROLIPOAMIDE SUCCINYLTRANSFERASE"),
        PdbEntryInfo("1e4e", "D-alanyl-D-lacate ligase"),
        PdbEntryInfo("1e58", "E.coli cofactor-dependent phosphoglycerate mutase"),
        PdbEntryInfo(
            "1e6e",
            "ADRENODOXIN REDUCTASE/ADRENODOXIN COMPLEX OF MITOCHONDRIAL P450 SYSTEMS"
        ),
        PdbEntryInfo(
            "1e6j",
            "Crystal structure of HIV-1 capsid protein (p24) in complex with Fab13B5"
        ),
        PdbEntryInfo("1e79", "Bovine F1-ATPase inhibited by DCCD (dicyclohexylcarbodiimide)"),
        PdbEntryInfo("1e7i", "HUMAN SERUM ALBUMIN COMPLEXED WITH OCTADECANOIC ACID (STEARIC ACID)"),
        PdbEntryInfo(
            "1e9y",
            "Crystal structure of Helicobacter pylori urease in complex with acetohydroxamic acid"
        ),
        PdbEntryInfo(
            "1ea1",
            "Cytochrome P450 14 alpha-sterol demethylase (CYP51) from Mycobacterium tuberculosis in complex with fluconazole"
        ),
        PdbEntryInfo(
            "1eaa",
            "ATOMIC STRUCTURE OF THE CUBIC CORE OF THE PYRUVATE DEHYDROGENASE MULTIENZYME COMPLEX"
        ),
        PdbEntryInfo(
            "1ebd",
            "DIHYDROLIPOAMIDE DEHYDROGENASE COMPLEXED WITH THE BINDING DOMAIN OF THE DIHYDROLIPOAMIDE ACETYLASE"
        ),
        PdbEntryInfo(
            "1ee5",
            "YEAST KARYOPHERIN (IMPORTIN) ALPHA IN A COMPLEX WITH A NUCLEOPLASMIN NLS PEPTIDE"
        ),
        PdbEntryInfo(
            "1efa",
            "CRYSTAL STRUCTURE OF THE LAC REPRESSOR DIMER BOUND TO OPERATOR AND THE ANTI-INDUCER ONPF"
        ),
        PdbEntryInfo(
            "1eft",
            "THE CRYSTAL STRUCTURE OF ELONGATION FACTOR EF-TU FROM THERMUS AQUATICUS IN THE GTP CONFORMATION"
        ),
        PdbEntryInfo("1efu", "ELONGATION FACTOR COMPLEX EF-TU/EF-TS FROM ESCHERICHIA COLI"),
        PdbEntryInfo(
            "1egf",
            "SOLUTION STRUCTURE OF MURINE EPIDERMAL GROWTH FACTOR DETERMINED BY NMR SPECTROSCOPY AND REFINED BY ENERGY MINIMIZATION WITH RESTRAINTS"
        ),
        PdbEntryInfo(
            "1ei1",
            "DIMERIZATION OF E. COLI DNA GYRASE B PROVIDES A STRUCTURAL MECHANISM FOR ACTIVATING THE ATPASE CATALYTIC CENTER"
        ),
        PdbEntryInfo("1ei7", "TMV COAT PROTEIN REFINED FROM THE 4-LAYER AGGREGATE"),
        PdbEntryInfo(
            "1eiy",
            "THE CRYSTAL STRUCTURE OF PHENYLALANYL-TRNA SYNTHETASE FROM THERMUS THERMOPHILUS COMPLEXED WITH COGNATE TRNAPHE"
        ),
        PdbEntryInfo(
            "1ej1",
            "COCRYSTAL STRUCTURE OF THE MESSENGER RNA 5' CAP-BINDING PROTEIN (EIF4E) BOUND TO 7-METHYL-GDP"
        ),
        PdbEntryInfo("1ej4", "COCRYSTAL STRUCTURE OF EIF4E/4E-BP1 PEPTIDE"),
        PdbEntryInfo("1ejh", "EIF4E/EIF4G PEPTIDE/7-METHYL-GDP"),
        PdbEntryInfo(
            "1ek9",
            "2.1A X-RAY STRUCTURE OF TOLC: AN INTEGRAL OUTER MEMBRANE PROTEIN AND EFFLUX PUMP COMPONENT FROM ESCHERICHIA COLI"
        ),
        PdbEntryInfo("1ema", "GREEN FLUORESCENT PROTEIN FROM AEQUOREA VICTORIA"),
        PdbEntryInfo("1eot", "SOLUTION NMR STRUCTURE OF EOTAXIN, MINIMIZED AVERAGE STRUCTURE"),
        PdbEntryInfo(
            "1eqj",
            "CRYSTAL STRUCTURE OF PHOSPHOGLYCERATE MUTASE FROM BACILLUS STEAROTHERMOPHILUS COMPLEXED WITH 2-PHOSPHOGLYCERATE"
        ),
        PdbEntryInfo(
            "1eri",
            "X-RAY STRUCTURE OF THE DNA-ECO RI ENDONUCLEASE-DNA RECOGNITION COMPLEX: THE RECOGNITION NETWORK AND THE INTEGRATION OF RECOGNITION AND CLEAVAGE"
        ),
        PdbEntryInfo(
            "1euq",
            "CRYSTAL STRUCTURE OF GLUTAMINYL-TRNA SYNTHETASE COMPLEXED WITH A TRNA-GLN MUTANT AND AN ACTIVE-SITE INHIBITOR"
        ),
        PdbEntryInfo(
            "1eve",
            "THREE DIMENSIONAL STRUCTURE OF THE ANTI-ALZHEIMER DRUG, E2020 (ARICEPT), COMPLEXED WITH ITS TARGET ACETYLCHOLINESTERASE"
        ),
        PdbEntryInfo(
            "1eww",
            "SOLUTION STRUCTURE OF SPRUCE BUDWORM ANTIFREEZE PROTEIN AT 30 DEGREES CELSIUS"
        ),
        PdbEntryInfo("1exr", "THE 1.0 ANGSTROM CRYSTAL STRUCTURE OF CA+2 BOUND CALMODULIN"),
        PdbEntryInfo(
            "1ezg",
            "CRYSTAL STRUCTURE OF ANTIFREEZE PROTEIN FROM THE BEETLE, TENEBRIO MOLITOR"
        ),
        PdbEntryInfo("1ezx", "CRYSTAL STRUCTURE OF A SERPIN:PROTEASE COMPLEX"),
        PdbEntryInfo(
            "1f1j",
            "CRYSTAL STRUCTURE OF CASPASE-7 IN COMPLEX WITH ACETYL-ASP-GLU-VAL-ASP-CHO"
        ),
        PdbEntryInfo("1f5a", "CRYSTAL STRUCTURE OF MAMMALIAN POLY(A) POLYMERASE"),
        PdbEntryInfo("1f6g", "POTASSIUM CHANNEL (KCSA) FULL-LENGTH FOLD"),
        PdbEntryInfo("1f88", "CRYSTAL STRUCTURE OF BOVINE RHODOPSIN"),
        PdbEntryInfo("1f9j", "STRUCTURE OF A NEW CRYSTAL FORM OF TETRAUBIQUITIN"),
        PdbEntryInfo(
            "1fa0",
            "STRUCTURE OF YEAST POLY(A) POLYMERASE BOUND TO MANGANATE AND 3'-DATP"
        ),
        PdbEntryInfo("1fa3", "SOLUTION STRUCTURE OF MNEI, A SWEET PROTEIN"),
        PdbEntryInfo("1far", "RAF-1 CYSTEINE RICH DOMAIN, NMR, MINIMIZED AVERAGE STRUCTURE"),
        PdbEntryInfo("1fbb", "CRYSTAL STRUCTURE OF NATIVE CONFORMATION OF BACTERIORHODOPSIN"),
        PdbEntryInfo("1fdh", "STRUCTURE OF HUMAN FOETAL DEOXYHAEMOGLOBIN"),
        PdbEntryInfo(
            "1fdl",
            "CRYSTALLOGRAPHIC REFINEMENT OF THE THREE-DIMENSIONAL STRUCTURE OF THE FAB D1.3-LYSOZYME COMPLEX AT 2.5-ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo("1feh", "FE-ONLY HYDROGENASE FROM CLOSTRIDIUM PASTEURIANUM"),
        PdbEntryInfo(
            "1ffk",
            "CRYSTAL STRUCTURE OF THE LARGE RIBOSOMAL SUBUNIT FROM HALOARCULA MARISMORTUI AT 2.4 ANGSTROM RESOLUTION"
        ),
        PdbEntryInfo("1ffx", "TUBULIN:STATHMIN-LIKE DOMAIN COMPLEX"),
        PdbEntryInfo(
            "1ffy",
            "INSIGHTS INTO EDITING FROM AN ILE-TRNA SYNTHETASE STRUCTURE WITH TRNA(ILE) AND MUPIROCIN"
        ),
        PdbEntryInfo(
            "1fg9",
            "3:1 COMPLEX OF INTERFERON-GAMMA RECEPTOR WITH INTERFERON-GAMMA DIMER"
        ),
        PdbEntryInfo(
            "1fha",
            "SOLVING THE STRUCTURE OF HUMAN H FERRITIN BY GENETICALLY ENGINEERING INTERMOLECULAR CRYSTAL CONTACTS"
        ),
        PdbEntryInfo("1fin", "CYCLIN A-CYCLIN-DEPENDENT KINASE 2 COMPLEX"),
        PdbEntryInfo("1fiq", "CRYSTAL STRUCTURE OF XANTHINE OXIDASE FROM BOVINE MILK"),
        PdbEntryInfo(
            "1fka",
            "STRUCTURE OF FUNCTIONALLY ACTIVATED SMALL RIBOSOMAL SUBUNIT AT 3.3 A RESOLUTION"
        ),
        PdbEntryInfo("1fkn", "Structure of Beta-Secretase Complexed with Inhibitor"),
        PdbEntryInfo(
            "1fnt",
            "CRYSTAL STRUCTURE OF THE 20S PROTEASOME FROM YEAST IN COMPLEX WITH THE PROTEASOME ACTIVATOR PA26 FROM TRYPANOSOME BRUCEI AT 3.2 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "1fo4",
            "CRYSTAL STRUCTURE OF XANTHINE DEHYDROGENASE ISOLATED FROM BOVINE MILK"
        ),
        PdbEntryInfo("1fok", "STRUCTURE OF RESTRICTION ENDONUCLEASE FOKI BOUND TO DNA"),
        PdbEntryInfo(
            "1fps",
            "CRYSTAL STRUCTURE OF RECOMBINANT FARNESYL DIPHOSPHATE SYNTHASE AT 2.6 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "1fpv",
            "STRUCTURE DETERMINATION OF FELINE PANLEUKOPENIA VIRUS EMPTY PARTICLES"
        ),
        PdbEntryInfo(
            "1fpy",
            "CRYSTAL STRUCTURE OF GLUTAMINE SYNTHETASE FROM SALMONELLA TYPHIMURIUM WITH INHIBITOR PHOSPHINOTHRICIN"
        ),
        PdbEntryInfo(
            "1fqv",
            "Insights into scf ubiquitin ligases from the structure of the skp1-skp2 complex"
        ),
        PdbEntryInfo(
            "1fqy",
            "STRUCTURE OF AQUAPORIN-1 AT 3.8 A RESOLUTION BY ELECTRON CRYSTALLOGRAPHY"
        ),
        PdbEntryInfo(
            "1fsd",
            "FULL SEQUENCE DESIGN 1 (FSD-1) OF BETA BETA ALPHA MOTIF, NMR, 41 STRUCTURES"
        ),
        PdbEntryInfo("1fuo", "FUMARASE C WITH BOUND CITRATE"),
        PdbEntryInfo("1fvi", "CRYSTAL STRUCTURE OF CHLORELLA VIRUS DNA LIGASE-ADENYLATE"),
        PdbEntryInfo("1fvm", "Complex of vancomycin with DI-acetyl-LYS-D-ALA-D-ALA"),
        PdbEntryInfo(
            "1fx8",
            "CRYSTAL STRUCTURE OF THE E. COLI GLYCEROL FACILITATOR (GLPF) WITH SUBSTRATE GLYCEROL"
        ),
        PdbEntryInfo("1fxk", "CRYSTAL STRUCTURE OF ARCHAEAL PREFOLDIN (GIMC)."),
        PdbEntryInfo("1fxt", "STRUCTURE OF A CONJUGATING ENZYME-UBIQUITIN THIOLESTER COMPLEX"),
        PdbEntryInfo(
            "1fyt",
            "CRYSTAL STRUCTURE OF A COMPLEX OF A HUMAN ALPHA/BETA-T CELL RECEPTOR, INFLUENZA HA ANTIGEN PEPTIDE, AND MHC CLASS II MOLECULE, HLA-DR1"
        ),
        PdbEntryInfo(
            "1fzc",
            "CRYSTAL STRUCTURE OF FRAGMENT DOUBLE-D FROM HUMAN FIBRIN WITH TWO DIFFERENT BOUND LIGANDS"
        ),
        PdbEntryInfo(
            "1g28",
            "STRUCTURE OF A FLAVIN-BINDING DOMAIN, LOV2, FROM THE CHIMERIC PHYTOCHROME/PHOTOTROPIN PHOTORECEPTOR PHY3"
        ),
        PdbEntryInfo("1g3i", "CRYSTAL STRUCTURE OF THE HSLUV PROTEASE-CHAPERONE COMPLEX"),
        PdbEntryInfo("1g4q", "RNA/DNA HYBRID DECAMER OF CAAAGAAAAG/CTTTTCTTTG"),
        PdbEntryInfo(
            "1g7k",
            "CRYSTAL STRUCTURE OF DSRED, A RED FLUORESCENT PROTEIN FROM DISCOSOMA SP. RED"
        ),
        PdbEntryInfo(
            "1g8h",
            "ATP SULFURYLASE FROM S. CEREVISIAE: THE TERNARY PRODUCT COMPLEX WITH APS AND PPI"
        ),
        PdbEntryInfo(
            "1gax",
            "CRYSTAL STRUCTURE OF THERMUS THERMOPHILUS VALYL-TRNA SYNTHETASE COMPLEXED WITH TRNA(VAL) AND VALYL-ADENYLATE ANALOGUE"
        ),
        PdbEntryInfo(
            "1gc1",
            "HIV-1 GP120 CORE COMPLEXED WITH CD4 AND A NEUTRALIZING HUMAN ANTIBODY"
        ),
        PdbEntryInfo("1gcn", "X-RAY ANALYSIS OF GLUCAGON AND ITS RELATIONSHIP TO RECEPTOR BINDING"),
        PdbEntryInfo("1gco", "CRYSTAL STRUCTURE OF GLUCOSE DEHYDROGENASE COMPLEXED WITH NAD+"),
        PdbEntryInfo("1gfl", "STRUCTURE OF GREEN FLUORESCENT PROTEIN"),
        PdbEntryInfo(
            "1gg2",
            "G PROTEIN HETEROTRIMER MUTANT GI_ALPHA_1(G203A) BETA_1 GAMMA_2 WITH GDP BOUND"
        ),
        PdbEntryInfo("1gh6", "RETINOBLASTOMA POCKET COMPLEXED WITH SV40 LARGE T ANTIGEN"),
        PdbEntryInfo(
            "1gia",
            "STRUCTURE OF ACTIVE CONFORMATIONS OF GIA1 AND THE MECHANISM OF GTP HYDROLYSIS"
        ),
        PdbEntryInfo(
            "1gnj",
            "HUMAN SERUM ALBUMIN COMPLEXED WITH cis-5,8,11,14-EICOSATETRAENOIC ACID (ARACHIDONIC ACID)"
        ),
        PdbEntryInfo(
            "1got",
            "HETEROTRIMERIC COMPLEX OF A GT-ALPHA/GI-ALPHA CHIMERA AND THE GT-BETA-GAMMA SUBUNITS"
        ),
        PdbEntryInfo(
            "1gp1",
            "THE REFINED STRUCTURE OF THE SELENOENZYME GLUTATHIONE PEROXIDASE AT 0.2-NM RESOLUTION"
        ),
        PdbEntryInfo(
            "1gpa",
            "STRUCTURAL MECHANISM FOR GLYCOGEN PHOSPHORYLASE CONTROL BY PHOSPHORYLATION AND AMP"
        ),
        PdbEntryInfo("1gpe", "GLUCOSE OXIDASE FROM PENICILLIUM AMAGASAKIENSE"),
        PdbEntryInfo("1gt0", "Crystal structure of a POU/HMG/DNA ternary complex"),
        PdbEntryInfo("1gtp", "GTP CYCLOHYDROLASE I"),
        PdbEntryInfo("1gtq", "6-PYRUVOYL TETRAHYDROPTERIN SYNTHASE"),
        PdbEntryInfo(
            "1gtr",
            "STRUCTURAL BASIS OF ANTICODON LOOP RECOGNITION BY GLUTAMINYL-TRNA SYNTHETASE"
        ),
        PdbEntryInfo("1gxp", "PhoB effector domain in complex with pho box DNA."),
        PdbEntryInfo("1gyu", "Gamma-adaptin appendage domain from clathrin adaptor AP1"),
        PdbEntryInfo("1h02", "Human Insulin-like growth factor; SRS Daresbury data"),
        PdbEntryInfo(
            "1h0r",
            "Type II Dehydroquinase from Mycobacterium tuberculosis complexed with 2,3-anhydro-quinic acid"
        ),
        PdbEntryInfo(
            "1h15",
            "X-ray crystal structure of HLA-DRA1*0101/DRB5*0101 complexed with a peptide from Epstein Barr Virus DNA polymerase"
        ),
        PdbEntryInfo(
            "1h2c",
            "Ebola virus matrix protein VP40 N-terminal domain in complex with RNA (High-resolution VP40[55-194] variant)."
        ),
        PdbEntryInfo("1h2n", "Factor Inhibiting HIF-1 alpha"),
        PdbEntryInfo("1h68", "sensory rhodopsin II"),
        PdbEntryInfo("1h76", "The crystal structure of diferric porcine serum transferrin"),
        PdbEntryInfo("1hbm", "METHYL-COENZYME M REDUCTASE ENZYME PRODUCT COMPLEX"),
        PdbEntryInfo(
            "1hco",
            "THE STRUCTURE OF HUMAN CARBONMONOXY HAEMOGLOBIN AT 2.7 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "1hcq",
            "THE CRYSTAL STRUCTURE OF THE ESTROGEN RECEPTOR DNA-BINDING DOMAIN BOUND TO DNA: HOW RECEPTORS DISCRIMINATE BETWEEN THEIR RESPONSE ELEMENTS"
        ),
        PdbEntryInfo("1he8", "Ras G12V - PI 3-kinase gamma complex"),
        PdbEntryInfo(
            "1hfe",
            "1.6 A RESOLUTION STRUCTURE OF THE FE-ONLY HYDROGENASE FROM DESULFOVIBRIO DESULFURICANS"
        ),
        PdbEntryInfo(
            "1hge",
            "BINDING OF INFLUENZA VIRUS HEMAGGLUTININ TO ANALOGS OF ITS CELL-SURFACE RECEPTOR, SIALIC ACID: ANALYSIS BY PROTON NUCLEAR MAGNETIC RESONANCE SPECTROSCOPY AND X-RAY CRYSTALLOGRAPHY"
        ),
        PdbEntryInfo("1hgu", "HUMAN GROWTH HORMONE"),
        PdbEntryInfo(
            "1hhg",
            "THE ANTIGENIC IDENTITY OF PEPTIDE(SLASH)MHC COMPLEXES: A COMPARISON OF THE CONFORMATION OF FIVE PEPTIDES PRESENTED BY HLA-A2"
        ),
        PdbEntryInfo(
            "1hhh",
            "THE ANTIGENIC IDENTITY OF PEPTIDE(SLASH)MHC COMPLEXES: A COMPARISON OF THE CONFORMATION OF FIVE PEPTIDES PRESENTED BY HLA-A2"
        ),
        PdbEntryInfo(
            "1hhi",
            "THE ANTIGENIC IDENTITY OF PEPTIDE(SLASH)MHC COMPLEXES: A COMPARISON OF THE CONFORMATION OF FIVE PEPTIDES PRESENTED BY HLA-A2"
        ),
        PdbEntryInfo(
            "1hhj",
            "THE ANTIGENIC IDENTITY OF PEPTIDE(SLASH)MHC COMPLEXES: A COMPARISON OF THE CONFORMATION OF FIVE PEPTIDES PRESENTED BY HLA-A2"
        ),
        PdbEntryInfo(
            "1hhk",
            "THE ANTIGENIC IDENTITY OF PEPTIDE(SLASH)MHC COMPLEXES: A COMPARISON OF THE CONFORMATION OF FIVE PEPTIDES PRESENTED BY HLA-A2"
        ),
        PdbEntryInfo("1hho", "STRUCTURE OF HUMAN OXYHAEMOGLOBIN AT 2.1 ANGSTROMS RESOLUTION"),
        PdbEntryInfo("1hkg", "STRUCTURAL DYNAMICS OF YEAST HEXOKINASE DURING CATALYSIS"),
        PdbEntryInfo(
            "1hlu",
            "STRUCTURE OF BOVINE BETA-ACTIN-PROFILIN COMPLEX WITH ACTIN BOUND ATP PHOSPHATES SOLVENT ACCESSIBLE"
        ),
        PdbEntryInfo(
            "1hmp",
            "THE CRYSTAL STRUCTURE OF HUMAN HYPOXANTHINE-GUANINE PHOSPHORIBOSYLTRANSFERASE WITH BOUND GMP"
        ),
        PdbEntryInfo(
            "1hny",
            "The structure of human pancreatic alpha-amylase at 1.8 angstroms resolution and comparisons with related enzymes"
        ),
        PdbEntryInfo(
            "1hox",
            "CRYSTAL STRUCTURE OF RABBIT PHOSPHOGLUCOSE ISOMERASE COMPLEXED WITH FRUCTOSE-6-PHOSPHATE"
        ),
        PdbEntryInfo(
            "1hrn",
            "HIGH RESOLUTION CRYSTAL STRUCTURES OF RECOMBINANT HUMAN RENIN IN COMPLEX WITH POLYHYDROXYMONOAMIDE INHIBITORS"
        ),
        PdbEntryInfo(
            "1hsa",
            "THE THREE-DIMENSIONAL STRUCTURE OF HLA-B27 AT 2.1 ANGSTROMS RESOLUTION SUGGESTS A GENERAL MECHANISM FOR TIGHT PEPTIDE BINDING TO MHC"
        ),
        PdbEntryInfo(
            "1hsg",
            "CRYSTAL STRUCTURE AT 1.9 ANGSTROMS RESOLUTION OF HUMAN IMMUNODEFICIENCY VIRUS (HIV) II PROTEASE COMPLEXED WITH L-735,524, AN ORALLY BIOAVAILABLE INHIBITOR OF THE HIV PROTEASES"
        ),
        PdbEntryInfo(
            "1htb",
            "CRYSTALLIZATION OF HUMAN BETA3 ALCOHOL DEHYDROGENASE (10 MG/ML) IN 100 MM SODIUM PHOSPHATE (PH 7.5), 7.5 MM NAD+ AND 1 MM 4-IODOPYRAZOLE AT 25 C"
        ),
        PdbEntryInfo("1htm", "STRUCTURE OF INFLUENZA HAEMAGGLUTININ AT THE PH OF MEMBRANE FUSION"),
        PdbEntryInfo(
            "1huy",
            "CRYSTAL STRUCTURE OF CITRINE, AN IMPROVED YELLOW VARIANT OF GREEN FLUORESCENT PROTEIN"
        ),
        PdbEntryInfo(
            "1hvb",
            "CRYSTAL STRUCTURE OF STREPTOMYCES R61 DD-PEPTIDASE COMPLEXED WITH A NOVEL CEPHALOSPORIN ANALOG OF CELL WALL PEPTIDOGLYCAN"
        ),
        PdbEntryInfo(
            "1hwg",
            "1:2 COMPLEX OF HUMAN GROWTH HORMONE WITH ITS SOLUBLE BINDING PROTEIN"
        ),
        PdbEntryInfo(
            "1hwh",
            "1:1 COMPLEX OF HUMAN GROWTH HORMONE MUTANT G120R WITH ITS SOLUBLE BINDING PROTEIN"
        ),
        PdbEntryInfo(
            "1hwk",
            "COMPLEX OF THE CATALYTIC PORTION OF HUMAN HMG-COA REDUCTASE WITH ATORVASTATIN"
        ),
        PdbEntryInfo("1hxb", "HIV-1 proteinase complexed with RO 31-8959"),
        PdbEntryInfo("1hxw", "HIV-1 PROTEASE DIMER COMPLEXED WITH A-84538"),
        PdbEntryInfo(
            "1hy1",
            "CRYSTAL STRUCTURE OF WILD TYPE DUCK DELTA 2 CRYSTALLIN (EYE LENS PROTEIN)"
        ),
        PdbEntryInfo(
            "1hzh",
            "CRYSTAL STRUCTURE OF THE INTACT HUMAN IGG B12 WITH BROAD AND POTENT ACTIVITY AGAINST PRIMARY HIV-1 ISOLATES: A TEMPLATE FOR HIV VACCINE DESIGN"
        ),
        PdbEntryInfo(
            "1i01",
            "CRYSTAL STRUCTURE OF BETA-KETOACYL [ACYL CARRIER PROTEIN] REDUCTASE FROM E. COLI."
        ),
        PdbEntryInfo("1i3d", "HUMAN CARBONMONOXY HEMOGLOBIN BART'S (GAMMA4)"),
        PdbEntryInfo("1i6h", "RNA POLYMERASE II ELONGATION COMPLEX"),
        PdbEntryInfo("1i7x", "BETA-CATENIN/E-CADHERIN COMPLEX"),
        PdbEntryInfo(
            "1i9a",
            "STRUCTURAL STUDIES OF CHOLESTEROL BIOSYNTHESIS: MEVALONATE 5-DIPHOSPHATE DECARBOXYLASE AND ISOPENTENYL DIPHOSPHATE ISOMERASE"
        ),
        PdbEntryInfo(
            "1ibn",
            "NMR STRUCTURE OF HEMAGGLUTININ FUSION PEPTIDE IN DPC MICELLES AT PH 5"
        ),
        PdbEntryInfo("1ice", "STRUCTURE AND MECHANISM OF INTERLEUKIN-1BETA CONVERTING ENZYME"),
        PdbEntryInfo(
            "1idc",
            "ISOCITRATE DEHYDROGENASE FROM E.COLI (MUTANT K230M), STEADY-STATE INTERMEDIATE COMPLEX DETERMINED BY LAUE CRYSTALLOGRAPHY"
        ),
        PdbEntryInfo(
            "1ide",
            "ISOCITRATE DEHYDROGENASE Y160F MUTANT STEADY-STATE INTERMEDIATE COMPLEX (LAUE DETERMINATION)"
        ),
        PdbEntryInfo(
            "1idr",
            "CRYSTAL STRUCTURE OF THE TRUNCATED-HEMOGLOBIN-N FROM MYCOBACTERIUM TUBERCULOSIS"
        ),
        PdbEntryInfo(
            "1ig8",
            "Crystal Structure of Yeast Hexokinase PII with the correct amino acid sequence"
        ),
        PdbEntryInfo("1igt", "STRUCTURE OF IMMUNOGLOBULIN"),
        PdbEntryInfo("1igy", "STRUCTURE OF IMMUNOGLOBULIN"),
        PdbEntryInfo("1ihp", "STRUCTURE OF PHOSPHOMONOESTERASE"),
        PdbEntryInfo("1ik9", "CRYSTAL STRUCTURE OF A XRCC4-DNA LIGASE IV COMPLEX"),
        PdbEntryInfo(
            "1ika",
            "STRUCTURE OF ISOCITRATE DEHYDROGENASE WITH ALPHA-KETOGLUTARATE AT 2.7 ANGSTROMS RESOLUTION: CONFORMATIONAL CHANGES INDUCED BY DECARBOXYLATION OF ISOCITRATE"
        ),
        PdbEntryInfo(
            "1iod",
            "CRYSTAL STRUCTURE OF THE COMPLEX BETWEEN THE COAGULATION FACTOR X BINDING PROTEIN FROM SNAKE VENOM AND THE GLA DOMAIN OF FACTOR X"
        ),
        PdbEntryInfo("1iph", "STRUCTURE OF CATALASE HPII FROM ESCHERICHIA COLI"),
        PdbEntryInfo(
            "1ir3",
            "PHOSPHORYLATED INSULIN RECEPTOR TYROSINE KINASE IN COMPLEX WITH PEPTIDE SUBSTRATE AND ATP ANALOG"
        ),
        PdbEntryInfo(
            "1irk",
            "CRYSTAL STRUCTURE OF THE TYROSINE KINASE DOMAIN OF THE HUMAN INSULIN RECEPTOR"
        ),
        PdbEntryInfo("1itf", "INTERFERON ALPHA-2A, NMR, 24 STRUCTURES"),
        PdbEntryInfo("1iwg", "Crystal structure of Bacterial Multidrug Efflux transporter AcrB"),
        PdbEntryInfo("1iwo", "Crystal structure of the SR Ca2+-ATPase in the absence of Ca2+"),
        PdbEntryInfo(
            "1iyt",
            "Solution structure of the Alzheimer's disease amyloid beta-peptide (1-42)"
        ),
        PdbEntryInfo(
            "1j3h",
            "Crystal structure of apoenzyme cAMP-dependent protein kinase catalytic subunit"
        ),
        PdbEntryInfo(
            "1j4e",
            "FRUCTOSE-1,6-BISPHOSPHATE ALDOLASE COVALENTLY BOUND TO THE SUBSTRATE DIHYDROXYACETONE PHOSPHATE"
        ),
        PdbEntryInfo(
            "1j59",
            "CATABOLITE GENE ACTIVATOR PROTEIN (CAP)/DNA COMPLEX + ADENOSINE-3',5'-CYCLIC-MONOPHOSPHATE"
        ),
        PdbEntryInfo("1j78", "Crystallographic analysis of the human vitamin D binding protein"),
        PdbEntryInfo(
            "1j8u",
            "Catalytic Domain of Human Phenylalanine Hydroxylase Fe(II) in Complex with Tetrahydrobiopterin"
        ),
        PdbEntryInfo(
            "1jb0",
            "Crystal Structure of Photosystem I: a Photosynthetic Reaction Center and Core Antenna System from Cyanobacteria"
        ),
        PdbEntryInfo("1jey", "Crystal Structure of the Ku heterodimer bound to DNA"),
        PdbEntryInfo(
            "1jff",
            "Refined structure of alpha-beta tubulin from zinc-induced sheets stabilized with taxol"
        ),
        PdbEntryInfo("1jfi", "Crystal Structure of the NC2-TBP-DNA Ternary Complex"),
        PdbEntryInfo(
            "1jgj",
            "CRYSTAL STRUCTURE OF SENSORY RHODOPSIN II AT 2.4 ANGSTROMS: INSIGHTS INTO COLOR TUNING AND TRANSDUCER INTERACTION"
        ),
        PdbEntryInfo(
            "1jku",
            "Crystal Structure of Manganese Catalase from Lactobacillus plantarum"
        ),
        PdbEntryInfo(
            "1jky",
            "Crystal Structure of the Anthrax Lethal Factor (LF): Wild-type LF Complexed with the N-terminal Sequence of MAPKK2"
        ),
        PdbEntryInfo(
            "1jl4",
            "CRYSTAL STRUCTURE OF THE HUMAN CD4 N-TERMINAL TWO DOMAIN FRAGMENT COMPLEXED TO A CLASS II MHC MOLECULE"
        ),
        PdbEntryInfo(
            "1jlb",
            "CRYSTAL STRUCTURE OF Y181C MUTANT HIV-1 REVERSE TRANSCRIPTASE IN COMPLEX WITH NEVIRAPINE"
        ),
        PdbEntryInfo(
            "1jlu",
            "Crystal Structure of the Catalytic Subunit of cAMP-dependent Protein Kinase Complexed with a Phosphorylated Substrate Peptide and Detergent"
        ),
        PdbEntryInfo("1jmb", "CRYSTAL STRUCTURE OF FOUR-HELIX BUNDLE MODEL"),
        PdbEntryInfo("1jnu", "Photoexcited structure of the plant photoreceptor domain, phy3 LOV2"),
        PdbEntryInfo(
            "1joc",
            "EEA1 homodimer of C-terminal FYVE domain bound to inositol 1,3-diphosphate"
        ),
        PdbEntryInfo("1jrj", "Solution structure of exendin-4 in 30-vol% trifluoroethanol"),
        PdbEntryInfo(
            "1jrp",
            "Crystal Structure of Xanthine Dehydrogenase inhibited by alloxanthine from Rhodobacter capsulatus"
        ),
        PdbEntryInfo("1jsp", "NMR Structure of CBP Bromodomain in complex with p53 peptide"),
        PdbEntryInfo("1jsu", "P27(KIP1)/CYCLIN A/CDK2 COMPLEX"),
        PdbEntryInfo("1jud", "L-2-HALOACID DEHALOGENASE"),
        PdbEntryInfo(
            "1jv2",
            "CRYSTAL STRUCTURE OF THE EXTRACELLULAR SEGMENT OF INTEGRIN ALPHAVBETA3"
        ),
        PdbEntryInfo(
            "1jva",
            "CRYSTAL STRUCTURE OF THE VMA1-DERIVED ENDONUCLEASE BEARING THE N AND C EXTEIN PROPEPTIDES"
        ),
        PdbEntryInfo("1jz7", "E. COLI (lacZ) BETA-GALACTOSIDASE IN COMPLEX WITH GALACTOSE"),
        PdbEntryInfo(
            "1jz8",
            "E. COLI (lacZ) BETA-GALACTOSIDASE (E537Q) IN COMPLEX WITH ALLOLACTOSE"
        ),
        PdbEntryInfo("1k4c", "Potassium Channel KcsA-Fab complex in high concentration of K+"),
        PdbEntryInfo("1k4r", "Structure of Dengue Virus"),
        PdbEntryInfo(
            "1k4t",
            "HUMAN DNA TOPOISOMERASE I (70 KDA) IN COMPLEX WITH THE POISON TOPOTECAN AND COVALENT COMPLEX WITH A 22 BASE PAIR DNA DUPLEX"
        ),
        PdbEntryInfo("1k5j", "The Crystal Structure of Nucleoplasmin-Core"),
        PdbEntryInfo(
            "1k83",
            "Crystal Structure of Yeast RNA Polymerase II Complexed with the Inhibitor Alpha Amanitin"
        ),
        PdbEntryInfo("1k88", "Crystal structure of procaspase-7"),
        PdbEntryInfo(
            "1k8t",
            "Crystal structure of the adenylyl cyclase domain of anthrax edema factor (EF)"
        ),
        PdbEntryInfo(
            "1k90",
            "Crystal structure of the adenylyl cyclase domain of anthrax edema factor (EF) in complex with calmodulin and 3' deoxy-ATP"
        ),
        PdbEntryInfo(
            "1k93",
            "Crystal structure of the adenylyl cyclase domain of anthrax edema factor (EF) in complex with calmodulin"
        ),
        PdbEntryInfo("1k9o", "CRYSTAL STRUCTURE OF MICHAELIS SERPIN-TRYPSIN COMPLEX"),
        PdbEntryInfo(
            "1kac",
            "KNOB DOMAIN FROM ADENOVIRUS SEROTYPE 12 IN COMPLEX WITH DOMAIN 1 OF ITS CELLULAR RECEPTOR CAR"
        ),
        PdbEntryInfo("1kas", "BETA-KETOACYL-ACP SYNTHASE II FROM ESCHERICHIA COLI"),
        PdbEntryInfo(
            "1kbh",
            "Mutual Synergistic Folding in the Interaction Between Nuclear Receptor Coactivators CBP and ACTR"
        ),
        PdbEntryInfo(
            "1kdf",
            "NORTH-ATLANTIC OCEAN POUT ANTIFREEZE PROTEIN TYPE III ISOFORM HPLC12 MUTANT, NMR, MINIMIZED AVERAGE STRUCTURE"
        ),
        PdbEntryInfo(
            "1kdx",
            "KIX DOMAIN OF MOUSE CBP (CREB BINDING PROTEIN) IN COMPLEX WITH PHOSPHORYLATED KINASE INDUCIBLE DOMAIN (PKID) OF RAT CREB (CYCLIC AMP RESPONSE ELEMENT BINDING PROTEIN), NMR 17 STRUCTURES"
        ),
        PdbEntryInfo(
            "1kgs",
            "Crystal Structure at 1.50 A of an OmpR/PhoB Homolog from Thermotoga maritima"
        ),
        PdbEntryInfo("1kil", "Three-dimensional structure of the complexin/SNARE complex"),
        PdbEntryInfo("1kln", "DNA POLYMERASE I KLENOW FRAGMENT (E.C.2.7.7.7) MUTANT/DNA COMPLEX"),
        PdbEntryInfo("1kny", "KANAMYCIN NUCLEOTIDYLTRANSFERASE"),
        PdbEntryInfo("1krv", "Galactoside Acetyltransferase in Complex with CoA and PNP-beta-Gal"),
        PdbEntryInfo(
            "1ky7",
            "THE AP-2 CLATHRIN ADAPTOR ALPHA-APPENDAGE IN COMPLEX WITH AMPHIPHYSIN FXDXF"
        ),
        PdbEntryInfo("1kyo", "YEAST CYTOCHROME BC1 COMPLEX WITH BOUND SUBSTRATE CYTOCHROME C"),
        PdbEntryInfo("1kys", "Crystal Structure of a Zn-bound Green Fluorescent Protein Biosensor"),
        PdbEntryInfo("1l0i", "Crystal structure of butyryl-ACP I62M mutant"),
        PdbEntryInfo("1l2p", "ATP Synthase b Subunit Dimerization Domain"),
        PdbEntryInfo("1l2y", "NMR Structure of Trp-Cage Miniprotein Construct TC5b"),
        PdbEntryInfo(
            "1l35",
            "STRUCTURE OF A THERMOSTABLE DISULFIDE-BRIDGE MUTANT OF PHAGE T4 LYSOZYME SHOWS THAT AN ENGINEERED CROSSLINK IN A FLEXIBLE REGION DOES NOT INCREASE THE RIGIDITY OF THE FOLDED PROTEIN"
        ),
        PdbEntryInfo("1l3w", "C-cadherin Ectodomain"),
        PdbEntryInfo(
            "1l8c",
            "STRUCTURAL BASIS FOR HIF-1ALPHA/CBP RECOGNITION IN THE CELLULAR HYPOXIC RESPONSE"
        ),
        PdbEntryInfo(
            "1l8t",
            "Crystal Structure Of 3',5\"-Aminoglycoside Phosphotransferase Type IIIa ADP Kanamycin A Complex"
        ),
        PdbEntryInfo("1l9k", "dengue methyltransferase"),
        PdbEntryInfo(
            "1lac",
            "THREE-DIMENSIONAL STRUCTURE OF THE LIPOYL DOMAIN FROM BACILLUS STEAROTHERMOPHILUS PYRUVATE DEHYDROGENASE MULTIENZYME COMPLEX"
        ),
        PdbEntryInfo(
            "1lb2",
            "Structure of the E. coli alpha C-terminal domain of RNA polymerase in complex with CAP and DNA"
        ),
        PdbEntryInfo(
            "1ldk",
            "Structure of the Cul1-Rbx1-Skp1-F boxSkp2 SCF Ubiquitin Ligase Complex"
        ),
        PdbEntryInfo("1lfg", "Structure of diferric human lactoferrin"),
        PdbEntryInfo("1lhs", "LOGGERHEAD SEA TURTLE MYOGLOBIN (AQUO-MET)"),
        PdbEntryInfo("1lm8", "Structure of a HIF-1a-pVHL-ElonginB-ElonginC Complex"),
        PdbEntryInfo("1lnq", "CRYSTAL STRUCTURE OF MTHK AT 3.3 A"),
        PdbEntryInfo("1lph", "LYS(B28)PRO(B29)-HUMAN INSULIN"),
        PdbEntryInfo(
            "1lqb",
            "Crystal structure of a hydroxylated HIF-1 alpha peptide bound to the pVHL/elongin-C/elongin-B complex"
        ),
        PdbEntryInfo(
            "1ltb",
            "2.6 ANGSTROMS CRYSTAL STRUCTURE OF PARTIALLY-ACTIVATED E. COLI HEAT-LABILE ENTEROTOXIN (LT)"
        ),
        PdbEntryInfo(
            "1lth",
            "T AND R STATES IN THE CRYSTALS OF BACTERIAL L-LACTATE DEHYDROGENASE REVEAL THE MECHANISM FOR ALLOSTERIC CONTROL"
        ),
        PdbEntryInfo(
            "1ltt",
            "LACTOSE BINDING TO HEAT-LABILE ENTEROTOXIN REVEALED BY X-RAY CRYSTALLOGRAPHY"
        ),
        PdbEntryInfo(
            "1lu1",
            "THE STRUCTURE OF THE DOLICHOS BIFLORUS SEED LECTIN IN COMPLEX WITH THE FORSSMAN DISACCHARIDE"
        ),
        PdbEntryInfo(
            "1lws",
            "Crystal structure of the intein homing endonuclease PI-SceI bound to its recognition sequence"
        ),
        PdbEntryInfo(
            "1lyb",
            "CRYSTAL STRUCTURES OF NATIVE AND INHIBITED FORMS OF HUMAN CATHEPSIN D: IMPLICATIONS FOR LYSOSOMAL TARGETING AND DRUG DESIGN"
        ),
        PdbEntryInfo(
            "1lyd",
            "CRYSTAL STRUCTURE OF T4-LYSOZYME GENERATED FROM SYNTHETIC CODING DNA EXPRESSED IN ESCHERICHIA COLI"
        ),
        PdbEntryInfo("1lyz", "Real-space refinement of the structure of hen egg-white lysozyme"),
        PdbEntryInfo("1lzi", "Glycosyltransferase A + UDP + H antigen acceptor"),
        PdbEntryInfo(
            "1m1j",
            "Crystal structure of native chicken fibrinogen with two different bound ligands"
        ),
        PdbEntryInfo("1m4h", "Crystal Structure of Beta-secretase complexed with Inhibitor OM00-3"),
        PdbEntryInfo(
            "1m7g",
            "Crystal structure of APS kinase from Penicillium Chrysogenum: Ternary structure with ADP and APS"
        ),
        PdbEntryInfo("1m8o", "Platelet integrin alfaIIb-beta3 cytoplasmic domain"),
        PdbEntryInfo("1mbn", "The stereochemistry of the protein myoglobin"),
        PdbEntryInfo(
            "1mbo",
            "Structure and refinement of oxymyoglobin at 1.6 angstroms resolution"
        ),
        PdbEntryInfo(
            "1mdt",
            "THE REFINED STRUCTURE OF MONOMERIC DIPHTHERIA TOXIN AT 2.3 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "1mel",
            "CRYSTAL STRUCTURE OF A CAMEL SINGLE-DOMAIN VH ANTIBODY FRAGMENT IN COMPLEX WITH LYSOZYME"
        ),
        PdbEntryInfo("1mh1", "SMALL G-PROTEIN"),
        PdbEntryInfo(
            "1mht",
            "COVALENT TERNARY STRUCTURE OF HHAI METHYLTRANSFERASE, DNA AND S-ADENOSYL-L-HOMOCYSTEINE"
        ),
        PdbEntryInfo("1miu", "Structure of a BRCA2-DSS1 complex"),
        PdbEntryInfo(
            "1mkx",
            "THE CO-CRYSTAL STRUCTURE OF UNLIGANDED BOVINE ALPHA-THROMBIN AND PRETHROMBIN-2: MOVEMENT OF THE YPPW SEGMENT AND ACTIVE SITE RESIDUES UPON LIGAND BINDING"
        ),
        PdbEntryInfo(
            "1mlc",
            "MONOCLONAL ANTIBODY FAB D44.1 RAISED AGAINST CHICKEN EGG-WHITE LYSOZYME COMPLEXED WITH LYSOZYME"
        ),
        PdbEntryInfo(
            "1mld",
            "REFINED STRUCTURE OF MITOCHONDRIAL MALATE DEHYDROGENASE FROM PORCINE HEART AND THE CONSENSUS STRUCTURE FOR DICARBOXYLIC ACID OXIDOREDUCTASES"
        ),
        PdbEntryInfo(
            "1mlw",
            "Crystal structure of human tryptophan hydroxylase with bound 7,8-dihydro-L-biopterin cofactor and Fe(III)"
        ),
        PdbEntryInfo(
            "1mme",
            "THE CRYSTAL STRUCTURE OF AN ALL-RNA HAMMERHEAD RIBOZYME: A PROPOSED MECHANISM FOR RNA CATALYTIC CLEAVAGE"
        ),
        PdbEntryInfo(
            "1mol",
            "TWO CRYSTAL STRUCTURES OF A POTENTLY SWEET PROTEIN: NATURAL MONELLIN AT 2.75 ANGSTROMS RESOLUTION AND SINGLE-CHAIN MONELLIN AT 1.7 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo("1mro", "METHYL-COENZYME M REDUCTASE"),
        PdbEntryInfo(
            "1mrr",
            "SUBSTITUTION OF MANGANESE FOR IRON IN RIBONUCLEOTIDE REDUCTASE FROM ESCHERICHIA COLI. SPECTROSCOPIC AND CRYSTALLOGRAPHIC CHARACTERIZATION"
        ),
        PdbEntryInfo(
            "1msd",
            "COMPARISON OF THE CRYSTAL STRUCTURES OF GENETICALLY ENGINEERED HUMAN MANGANESE SUPEROXIDE DISMUTASE AND MANGANESE SUPEROXIDE DISMUTASE FROM THERMUS THERMOPHILUS. DIFFERENCES IN DIMER-DIMER INTERACTIONS."
        ),
        PdbEntryInfo(
            "1msw",
            "Structural basis for the transition from initiation to elongation transcription in T7 RNA polymerase"
        ),
        PdbEntryInfo(
            "1muh",
            "CRYSTAL STRUCTURE OF TN5 TRANSPOSASE COMPLEXED WITH TRANSPOSON END DNA"
        ),
        PdbEntryInfo(
            "1mus",
            "crystal structure of Tn5 transposase complexed with resolved outside end DNA"
        ),
        PdbEntryInfo("1mwp", "N-TERMINAL DOMAIN OF THE AMYLOID PRECURSOR PROTEIN"),
        PdbEntryInfo(
            "1n0u",
            "Crystal structure of yeast elongation factor 2 in complex with sordarin"
        ),
        PdbEntryInfo("1n0v", "Crystal structure of elongation factor 2"),
        PdbEntryInfo("1n0w", "Crystal structure of a RAD51-BRCA2 BRC repeat complex"),
        PdbEntryInfo(
            "1n20",
            "(+)-Bornyl Diphosphate Synthase: Complex with Mg and 3-aza-2,3-dihydrogeranyl diphosphate"
        ),
        PdbEntryInfo("1n25", "Crystal structure of the SV40 Large T antigen helicase domain"),
        PdbEntryInfo(
            "1n2c",
            "NITROGENASE COMPLEX FROM AZOTOBACTER VINELANDII STABILIZED BY ADP-TETRAFLUOROALUMINATE"
        ),
        PdbEntryInfo("1n4e", "Crystal Structure of a DNA Decamer Containing a Thymine-dimer"),
        PdbEntryInfo("1n6g", "The structure of immature Dengue-2 prM particles"),
        PdbEntryInfo(
            "1n73",
            "Fibrin D-Dimer, Lamprey complexed with the PEPTIDE LIGAND: GLY-HIS-ARG-PRO-AMIDE"
        ),
        PdbEntryInfo(
            "1nca",
            "REFINED CRYSTAL STRUCTURE OF THE INFLUENZA VIRUS N9 NEURAMINIDASE-NC41 FAB COMPLEX"
        ),
        PdbEntryInfo("1nci", "STRUCTURAL BASIS OF CELL-CELL ADHESION BY CADHERINS"),
        PdbEntryInfo(
            "1nek",
            "Complex II (Succinate Dehydrogenase) From E. Coli with ubiquinone bound"
        ),
        PdbEntryInfo("1nkp", "Crystal structure of Myc-Max recognizing DNA"),
        PdbEntryInfo(
            "1nn2",
            "THREE-DIMENSIONAL STRUCTURE OF THE NEURAMINIDASE OF INFLUENZA VIRUS A(SLASH)TOKYO(SLASH)3(SLASH)67 AT 2.2 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "1nod",
            "MURINE INDUCIBLE NITRIC OXIDE SYNTHASE OXYGENASE DIMER (DELTA 65) WITH TETRAHYDROBIOPTERIN AND SUBSTRATE L-ARGININE"
        ),
        PdbEntryInfo("1noo", "CYTOCHROME P450-CAM COMPLEXED WITH 5-EXO-HYDROXYCAMPHOR"),
        PdbEntryInfo(
            "1nqo",
            "Glyceraldehyde-3-Phosphate Dehydrogenase Mutant With Cys 149 Replaced By Ser Complexed With Nad+ and D-Glyceraldehyde-3-Phosphate"
        ),
        PdbEntryInfo("1nsf", "D2 HEXAMERIZATION DOMAIN OF N-ETHYLMALEIMIDE SENSITIVE FACTOR (NSF)"),
        PdbEntryInfo(
            "1nu8",
            "Crystal structure of human dipeptidyl peptidase IV (DPP-IV) in complex with Diprotin A (IPI)"
        ),
        PdbEntryInfo("1nw9", "STRUCTURE OF CASPASE-9 IN AN INHIBITORY COMPLEX WITH XIAP-BIR3"),
        PdbEntryInfo("1nxg", "The F383A variant of type II Citrate Synthase complexed with NADH"),
        PdbEntryInfo(
            "1o4x",
            "TERNARY COMPLEX OF THE DNA BINDING DOMAINS OF THE OCT1 AND SOX2 TRANSCRIPTION FACTORS WITH A 19MER OLIGONUCLEOTIDE FROM THE HOXB1 REGULATORY ELEMENT"
        ),
        PdbEntryInfo("1o9j", "The X-ray crystal structure of eta-crystallin"),
        PdbEntryInfo("1oco", "BOVINE HEART CYTOCHROME C OXIDASE IN CARBON MONOXIDE-BOUND STATE"),
        PdbEntryInfo("1oei", "Human prion protein 61-84"),
        PdbEntryInfo("1ohf", "The refined structure of Nudaurelia capensis omega virus"),
        PdbEntryInfo("1ohg", "STRUCTURE OF THE DSDNA BACTERIOPHAGE HK97 MATURE EMPTY CAPSID"),
        PdbEntryInfo(
            "1ohr",
            "VIRACEPT (R) (NELFINAVIR MESYLATE, AG1343): A POTENT ORALLY BIOAVAILABLE INHIBITOR OF HIV-1 PROTEASE"
        ),
        PdbEntryInfo("1oj6", "Human brain neuroglobin three-dimensional structure"),
        PdbEntryInfo("1ojx", "Crystal structure of an Archaeal fructose 1,6-bisphosphate aldolase"),
        PdbEntryInfo(
            "1ok8",
            "Crystal structure of the dengue 2 virus envelope glycoprotein in the postfusion conformation"
        ),
        PdbEntryInfo(
            "1olg",
            "HIGH-RESOLUTION SOLUTION STRUCTURE OF THE OLIGOMERIZATION DOMAIN OF P53 BY MULTI-DIMENSIONAL NMR"
        ),
        PdbEntryInfo("1om4", "STRUCTURE OF RAT NEURONAL NOS HEME DOMAIN WITH L-ARGININE BOUND"),
        PdbEntryInfo("1opl", "Structural basis for the auto-inhibition of c-Abl tyrosine kinase"),
        PdbEntryInfo(
            "1owt",
            "Structure of the Alzheimer's disease amyloid precursor protein copper binding domain"
        ),
        PdbEntryInfo(
            "1oy8",
            "Structural Basis of Multiple Drug Binding Capacity of the AcrB Multidrug Efflux Pump"
        ),
        PdbEntryInfo(
            "1oyd",
            "Structural Basis of Multiple Binding Capacity of the AcrB multidrug Efflux Pump"
        ),
        PdbEntryInfo("1p0n", "IPP:DMAPP isomerase type II, FMN complex"),
        PdbEntryInfo(
            "1p58",
            "Complex Organization of Dengue Virus Membrane Proteins as Revealed by 9.5 Angstrom Cryo-EM reconstruction"
        ),
        PdbEntryInfo("1p8j", "CRYSTAL STRUCTURE OF THE PROPROTEIN CONVERTASE FURIN"),
        PdbEntryInfo("1pah", "HUMAN PHENYLALANINE HYDROXYLASE DIMER, RESIDUES 117-424"),
        PdbEntryInfo(
            "1pau",
            "Crystal structure of the complex of apopain with the tetrapeptide aldehyde inhibitor AC-DEVD-CHO"
        ),
        PdbEntryInfo("1phz", "STRUCTURE OF PHOSPHORYLATED PHENYLALANINE HYDROXYLASE"),
        PdbEntryInfo("1pkq", "Myelin Oligodendrocyte Glycoprotein-(8-18C5) Fab-complex"),
        PdbEntryInfo(
            "1plq",
            "CRYSTAL STRUCTURE OF THE EUKARYOTIC DNA POLYMERASE PROCESSIVITY FACTOR PCNA"
        ),
        PdbEntryInfo(
            "1pmb",
            "THE DETERMINATION OF THE CRYSTAL STRUCTURE OF RECOMBINANT PIG MYOGLOBIN BY MOLECULAR REPLACEMENT AND ITS REFINEMENT"
        ),
        PdbEntryInfo(
            "1pmr",
            "LIPOYL DOMAIN FROM THE DIHYDROLIPOYL SUCCINYLTRANSFERASE COMPONENT OF THE 2-OXOGLUTARATE DEHYDROGENASE MULTIENZYME COMPLEX OF ESCHERICHIA COLI, NMR, 25 STRUCTURES"
        ),
        PdbEntryInfo(
            "1pob",
            "CRYSTAL STRUCTURE OF COBRA-VENOM PHOSPHOLIPASE A2 IN A COMPLEX WITH A TRANSITION-STATE ANALOGUE"
        ),
        PdbEntryInfo(
            "1poc",
            "CRYSTAL STRUCTURE OF BEE-VENOM PHOSPHOLIPASE A2 IN A COMPLEX WITH A TRANSITION-STATE ANALOGUE"
        ),
        PdbEntryInfo(
            "1ppb",
            "THE REFINED 1.9 ANGSTROMS CRYSTAL STRUCTURE OF HUMAN ALPHA-THROMBIN: INTERACTION WITH D-PHE-PRO-ARG CHLOROMETHYLKETONE AND SIGNIFICANCE OF THE TYR-PRO-PRO-TRP INSERTION SEGMENT"
        ),
        PdbEntryInfo(
            "1ppi",
            "THE ACTIVE CENTER OF A MAMMALIAN ALPHA-AMYLASE. THE STRUCTURE OF THE COMPLEX OF A PANCREATIC ALPHA-AMYLASE WITH A CARBOHYDRATE INHIBITOR REFINED TO 2.2 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "1prc",
            "CRYSTALLOGRAPHIC REFINEMENT AT 2.3 ANGSTROMS RESOLUTION AND REFINED MODEL OF THE PHOTOSYNTHETIC REACTION CENTER FROM RHODOPSEUDOMONAS VIRIDIS"
        ),
        PdbEntryInfo(
            "1prh",
            "THE X-RAY CRYSTAL STRUCTURE OF THE MEMBRANE PROTEIN PROSTAGLANDIN H2 SYNTHASE-1"
        ),
        PdbEntryInfo("1prt", "THE CRYSTAL STRUCTURE OF PERTUSSIS TOXIN"),
        PdbEntryInfo(
            "1psh",
            "CRYSTAL STRUCTURE OF PHOSPHOLIPASE A2 FROM INDIAN COBRA REVEALS A TRIMERIC ASSOCIATION"
        ),
        PdbEntryInfo("1psi", "Intact recombined alpha1-antitrypsin mutant PHE 51 to LEU"),
        PdbEntryInfo(
            "1psv",
            "COMPUTATIONALLY DESIGNED PEPTIDE WITH A BETA-BETA-ALPHA FOLD SELECTION, NMR, 32 STRUCTURES"
        ),
        PdbEntryInfo(
            "1pth",
            "The Structural Basis of Aspirin Activity Inferred from the Crystal Structure of Inactivated Prostaglandin H2 Synthase"
        ),
        PdbEntryInfo(
            "1ptu",
            "CRYSTAL STRUCTURE OF PROTEIN TYROSINE PHOSPHATASE 1B COMPLEXED WITH PHOSPHOTYROSINE-CONTAINING HEXA-PEPTIDE (DADEPYL-NH2)"
        ),
        PdbEntryInfo("1pv6", "Crystal structure of lactose permease"),
        PdbEntryInfo(
            "1pw4",
            "Crystal Structure of the Glycerol-3-Phosphate Transporter from E.Coli"
        ),
        PdbEntryInfo(
            "1py1",
            "Complex of GGA1-VHS domain and beta-secretase C-terminal phosphopeptide"
        ),
        PdbEntryInfo("1pzn", "Rad51 (RadA)"),
        PdbEntryInfo(
            "1q0d",
            "Crystal structure of Ni-containing superoxide dismutase with Ni-ligation corresponding to the oxidized state"
        ),
        PdbEntryInfo("1q2w", "X-Ray Crystal Structure of the SARS Coronavirus Main Protease"),
        PdbEntryInfo(
            "1q5a",
            "S-shaped trans interactions of cadherins model based on fitting C-cadherin (1L3W) to 3D map of desmosomes obtained by electron tomography"
        ),
        PdbEntryInfo(
            "1q5c",
            "S-S-lambda-shaped TRANS and CIS interactions of cadherins model based on fitting C-cadherin (1L3W) to 3D map of desmosomes obtained by electron tomography"
        ),
        PdbEntryInfo("1q8h", "Crystal structure of porcine osteocalcin"),
        PdbEntryInfo(
            "1qf6",
            "STRUCTURE OF E. COLI THREONYL-TRNA SYNTHETASE COMPLEXED WITH ITS COGNATE TRNA"
        ),
        PdbEntryInfo(
            "1qfu",
            "INFLUENZA VIRUS HEMAGGLUTININ COMPLEXED WITH A NEUTRALIZING ANTIBODY"
        ),
        PdbEntryInfo(
            "1qgk",
            "STRUCTURE OF IMPORTIN BETA BOUND TO THE IBB DOMAIN OF IMPORTIN ALPHA"
        ),
        PdbEntryInfo(
            "1qiu",
            "A triple beta-spiral in the adenovirus fibre shaft reveals a new structural motif for biological fibres"
        ),
        PdbEntryInfo(
            "1qku",
            "WILD TYPE ESTROGEN NUCLEAR RECEPTOR LIGAND BINDING DOMAIN COMPLEXED WITH ESTRADIOL"
        ),
        PdbEntryInfo(
            "1qle",
            "CRYO-STRUCTURE OF THE PARACOCCUS DENITRIFICANS FOUR-SUBUNIT CYTOCHROME C OXIDASE IN THE COMPLETELY OXIDIZED STATE COMPLEXED WITH AN ANTIBODY FV FRAGMENT"
        ),
        PdbEntryInfo("1qm2", "Human prion protein fragment 121-230"),
        PdbEntryInfo("1qml", "Hg complex of yeast 5-aminolaevulinic acid dehydratase"),
        PdbEntryInfo("1qmz", "PHOSPHORYLATED CDK2-CYCLYIN A-SUBSTRATE PEPTIDE COMPLEX"),
        PdbEntryInfo("1qnv", "yeast 5-aminolaevulinic acid dehydratase Lead (Pb) complex"),
        PdbEntryInfo("1qqw", "CRYSTAL STRUCTURE OF HUMAN ERYTHROCYTE CATALASE"),
        PdbEntryInfo("1qu1", "CRYSTAL STRUCTURE OF EHA2 (23-185)"),
        PdbEntryInfo(
            "1qys",
            "Crystal structure of Top7: A computationally designed protein with a novel fold"
        ),
        PdbEntryInfo(
            "1r4i",
            "Crystal Structure of Androgen Receptor DNA-Binding Domain Bound to a Direct Repeat Response Element"
        ),
        PdbEntryInfo("1r4n", "APPBP1-UBA3-NEDD8, an E1-ubiquitin-like protein complex with ATP"),
        PdbEntryInfo(
            "1r4u",
            "URATE OXIDASE FROM ASPERGILLUS FLAVUS COMPLEXED WITH ITS INHIBITOR OXONIC ACID"
        ),
        PdbEntryInfo("1r5p", "Crystal Structure Analysis of KaiB from PCC7120"),
        PdbEntryInfo("1r7r", "The crystal structure of murine p97/VCP at 3.6A"),
        PdbEntryInfo(
            "1r8j",
            "Crystal Structure of Circadian Clock Protein KaiA from Synechococcus elongatus"
        ),
        PdbEntryInfo("1r9f", "Crystal structure of p19 complexed with 19-bp small interfering RNA"),
        PdbEntryInfo(
            "1rcx",
            "NON-ACTIVATED SPINACH RUBISCO IN COMPLEX WITH ITS SUBSTRATE RIBULOSE-1,5-BISPHOSPHATE"
        ),
        PdbEntryInfo(
            "1rd8",
            "Crystal Structure of the 1918 Human H1 Hemagglutinin Precursor (HA0)"
        ),
        PdbEntryInfo(
            "1rf8",
            "Solution structure of the yeast translation initiation factor eIF4E in complex with m7GDP and eIF4GI residues 393 to 490"
        ),
        PdbEntryInfo(
            "1rfb",
            "CRYSTAL STRUCTURE OF RECOMBINANT BOVINE INTERFERON-GAMMA AT 3.0 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo("1rh4", "RH4 DESIGNED RIGHT-HANDED COILED COIL TETRAMER"),
        PdbEntryInfo("1ri1", "Structure and mechanism of mRNA cap (guanine N-7) methyltransferase"),
        PdbEntryInfo(
            "1rlc",
            "CRYSTAL STRUCTURE OF THE UNACTIVATED RIBULOSE 1, 5-BISPHOSPHATE CARBOXYLASE(SLASH)OXYGENASE COMPLEXED WITH A TRANSITION STATE ANALOG, 2-CARBOXY-D-ARABINITOL 1,5-BISPHOSPHATE"
        ),
        PdbEntryInfo("1ron", "NMR SOLUTION STRUCTURE OF HUMAN NEUROPEPTIDE Y"),
        PdbEntryInfo(
            "1rtn",
            "PROTON NMR ASSIGNMENTS AND SOLUTION CONFORMATION OF RANTES, A CHEMOKINE OF THE CC TYPE"
        ),
        PdbEntryInfo("1ruz", "1918 H1 Hemagglutinin"),
        PdbEntryInfo("1rv1", "CRYSTAL STRUCTURE OF HUMAN MDM2 WITH AN IMIDAZOLINE INHIBITOR"),
        PdbEntryInfo(
            "1rva",
            "MG2+ BINDING TO THE ACTIVE SITE OF ECO RV ENDONUCLEASE: A CRYSTALLOGRAPHIC STUDY OF COMPLEXES WITH SUBSTRATE AND PRODUCT DNA AT 2 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "1rvc",
            "MG2+ BINDING TO THE ACTIVE SITE OF ECO RV ENDONUCLEASE: A CRYSTALLOGRAPHIC STUDY OF COMPLEXES WITH SUBSTRATE AND PRODUCT DNA AT 2 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo("1rvf", "FAB COMPLEXED WITH INTACT HUMAN RHINOVIRUS"),
        PdbEntryInfo(
            "1rwt",
            "Crystal Structure of Spinach Major Light-harvesting complex at 2.72 Angstrom Resolution"
        ),
        PdbEntryInfo(
            "1ryf",
            "Alternative Splicing of Rac1 Generates Rac1b, a Self-activating GTPase"
        ),
        PdbEntryInfo("1rys", "REPLICATION OF A CIS-SYN THYMINE DIMER AT ATOMIC RESOLUTION"),
        PdbEntryInfo("1s58", "The structure of B19 parvovirus capsid"),
        PdbEntryInfo("1s5l", "Architecture of the photosynthetic oxygen evolving center"),
        PdbEntryInfo(
            "1s9v",
            "Crystal structure of HLA-DQ2 complexed with deamidated gliadin peptide"
        ),
        PdbEntryInfo("1sbt", "ATOMIC COORDINATES FOR SUBTILISIN BPN (OR NOVO)"),
        PdbEntryInfo("1sep", "MOUSE SEPIAPTERIN REDUCTASE COMPLEXED WITH NADP AND SEPIAPTERIN"),
        PdbEntryInfo(
            "1set",
            "CRYSTAL STRUCTURES AT 2.5 ANGSTROMS RESOLUTION OF SERYL-TRNA SYNTHETASE COMPLEXED WITH TWO DIFFERENT ANALOGUES OF SERYL-ADENYLATE"
        ),
        PdbEntryInfo("1sfc", "NEURONAL SYNAPTIC FUSION COMPLEX"),
        PdbEntryInfo(
            "1sg1",
            "Crystal Structure of the Receptor-Ligand Complex between Nerve Growth Factor and the Common Neurotrophin Receptor p75"
        ),
        PdbEntryInfo("1sgz", "Crystal Structure of Unbound Beta-Secretase Catalytic Domain."),
        PdbEntryInfo(
            "1shr",
            "Crystal structure of ferrocyanide bound human hemoglobin A2 at 1.88A resolution"
        ),
        PdbEntryInfo(
            "1si3",
            "Crystal structure of the PAZ domain of human eIF2c1 in complex with a 9-mer siRNA-like duplex"
        ),
        PdbEntryInfo("1skh", "N-terminal (1-30) of bovine Prion protein"),
        PdbEntryInfo(
            "1sl2",
            "Ternary 5' complex of T7 DNA polymerase with a DNA primer/template containing a cis-syn thymine dimer on the template and an incoming nucleotide"
        ),
        PdbEntryInfo("1smd", "HUMAN SALIVARY AMYLASE"),
        PdbEntryInfo(
            "1sos",
            "ATOMIC STRUCTURES OF WILD-TYPE AND THERMOSTABLE MUTANT RECOMBINANT HUMAN CU, ZN SUPEROXIDE DISMUTASE"
        ),
        PdbEntryInfo("1st0", "Structure of DcpS bound to m7GpppG"),
        PdbEntryInfo("1su4", "Crystal structure of calcium ATPase with two bound calcium ions"),
        PdbEntryInfo("1sva", "SIMIAN VIRUS 40"),
        PdbEntryInfo(
            "1svm",
            "Co-crystal structure of SV40 large T antigen helicase domain and ATP"
        ),
        PdbEntryInfo("1szp", "A Crystal Structure of the Rad51 Filament"),
        PdbEntryInfo(
            "1t24",
            "Plasmodium falciparum lactate dehydrogenase complexed with NAD+ and 4-hydroxy-1,2,5-oxadiazole-3-carboxylic acid"
        ),
        PdbEntryInfo(
            "1t25",
            "Plasmodium falciparum lactate dehydrogenase complexed with NADH and 3-hydroxyisoxazole-4-carboxylic acid"
        ),
        PdbEntryInfo(
            "1t2k",
            "Structure Of The DNA Binding Domains Of IRF3, ATF-2 and Jun Bound To DNA"
        ),
        PdbEntryInfo(
            "1t6o",
            "Nucleocapsid-binding domain of the measles virus P protein (amino acids 457-507) in complex with amino acids 486-505 of the measles virus N protein"
        ),
        PdbEntryInfo(
            "1t8u",
            "Crystal Structure of human 3-O-Sulfotransferase-3 with bound PAP and tetrasaccharide substrate"
        ),
        PdbEntryInfo("1tau", "TAQ POLYMERASE (E.C.2.7.7.7)/DNA/B-OCTYLGLUCOSIDE COMPLEX"),
        PdbEntryInfo(
            "1tbd",
            "SOLUTION STRUCTURE OF THE ORIGIN DNA BINDING DOMAIN OF SV40 T-ANTIGEN, NMR, MINIMIZED AVERAGE STRUCTURE"
        ),
        PdbEntryInfo("1tbg", "BETA-GAMMA DIMER OF THE HETEROTRIMERIC G-PROTEIN TRANSDUCIN"),
        PdbEntryInfo("1tcf", "CRYSTAL STRUCTURE OF CALCIUM-SATURATED RABBIT SKELETAL TROPONIN C"),
        PdbEntryInfo("1tcr", "MURINE T-CELL ANTIGEN RECEPTOR 2C CLONE"),
        PdbEntryInfo("1tez", "COMPLEX BETWEEN DNA AND THE DNA PHOTOLYASE FROM ANACYSTIS NIDULANS"),
        PdbEntryInfo(
            "1tf6",
            "CO-CRYSTAL STRUCTURE OF XENOPUS TFIIIA ZINC FINGER DOMAIN BOUND TO THE 5S RIBOSOMAL RNA GENE INTERNAL CONTROL REGION"
        ),
        PdbEntryInfo("1tgh", "TATA BINDING PROTEIN (TBP)/DNA COMPLEX"),
        PdbEntryInfo(
            "1tgs",
            "THREE-DIMENSIONAL STRUCTURE OF THE COMPLEX BETWEEN PANCREATIC SECRETORY INHIBITOR (KAZAL TYPE) AND TRYPSINOGEN AT 1.8 ANGSTROMS RESOLUTION. STRUCTURE SOLUTION, CRYSTALLOGRAPHIC REFINEMENT AND PRELIMINARY STRUCTURAL INTERPRETATION"
        ),
        PdbEntryInfo(
            "1tha",
            "MECHANISM OF MOLECULAR RECOGNITION. STRUCTURAL ASPECTS OF 3,3'-DIIODO-L-THYRONINE BINDING TO HUMAN SERUM TRANSTHYRETIN"
        ),
        PdbEntryInfo("1thj", "CARBONIC ANHYDRASE FROM METHANOSARCINA"),
        PdbEntryInfo(
            "1thv",
            "THE STRUCTURES OF THREE CRYSTAL FORMS OF THE SWEET PROTEIN THAUMATIN"
        ),
        PdbEntryInfo(
            "1tki",
            "AUTOINHIBITED SERINE KINASE DOMAIN OF THE GIANT MUSCLE PROTEIN TITIN"
        ),
        PdbEntryInfo(
            "1tlf",
            "UNPRECEDENTED QUATERNARY STRUCTURE OF E. COLI LAC REPRESSOR CORE TETRAMER: IMPLICATIONS FOR DNA LOOPING"
        ),
        PdbEntryInfo(
            "1tll",
            "CRYSTAL STRUCTURE OF RAT NEURONAL NITRIC-OXIDE SYNTHASE REDUCTASE MODULE AT 2.3 A RESOLUTION."
        ),
        PdbEntryInfo(
            "1trz",
            "CRYSTALLOGRAPHIC EVIDENCE FOR DUAL COORDINATION AROUND ZINC IN THE T3R3 HUMAN INSULIN HEXAMER"
        ),
        PdbEntryInfo(
            "1ttd",
            "SOLUTION-STATE STRUCTURE OF A DNA DODECAMER DUPLEX CONTAINING A CIS-SYN THYMINE CYCLOBUTANE DIMER"
        ),
        PdbEntryInfo("1ttt", "Phe-tRNA, elongation factoR EF-TU:GDPNP ternary complex"),
        PdbEntryInfo("1tub", "TUBULIN ALPHA-BETA DIMER, ELECTRON DIFFRACTION"),
        PdbEntryInfo("1tui", "INTACT ELONGATION FACTOR TU IN COMPLEX WITH GDP"),
        PdbEntryInfo("1tup", "TUMOR SUPPRESSOR P53 COMPLEXED WITH DNA"),
        PdbEntryInfo("1u04", "Crystal structure of full length Argonaute from Pyrococcus furiosus"),
        PdbEntryInfo("1u19", "Crystal Structure of Bovine Rhodopsin at 2.2 Angstroms Resolution"),
        PdbEntryInfo("1u1z", "The Structure of (3R)-hydroxyacyl-ACP dehydratase (FabZ)"),
        PdbEntryInfo("1u6b", "CRYSTAL STRUCTURE OF A SELF-SPLICING GROUP I INTRON WITH BOTH EXONS"),
        PdbEntryInfo("1ubq", "STRUCTURE OF UBIQUITIN REFINED AT 1.8 ANGSTROMS RESOLUTION"),
        PdbEntryInfo("1ui9", "Crystal analysis of chorismate mutase from thermus thermophilus"),
        PdbEntryInfo("1ul1", "Crystal structure of the human FEN1-PCNA complex"),
        PdbEntryInfo(
            "1um2",
            "Crystal Structure of the Vma1-Derived Endonuclease with the Ligated Extein Segment"
        ),
        PdbEntryInfo("1ump", "GEOMETRY OF TRITERPENE CONVERSION TO PENTACARBOCYCLIC HOPENE"),
        PdbEntryInfo(
            "1un6",
            "THE CRYSTAL STRUCTURE OF A ZINC FINGER - RNA COMPLEX REVEALS TWO MODES OF MOLECULAR RECOGNITION"
        ),
        PdbEntryInfo(
            "1ut0",
            "CRYSTAL STRUCTURE OF CYTOGLOBIN: THE FOURTH GLOBIN TYPE DISCOVERED IN MAN DISPLAYS HEME HEXA-COORDINATION"
        ),
        PdbEntryInfo(
            "1uv6",
            "X-ray structure of acetylcholine binding protein (AChBP) in complex with carbamylcholine"
        ),
        PdbEntryInfo("1uwh", "The complex of wild type B-RAF and BAY439006."),
        PdbEntryInfo("1v0d", "Crystal Structure of Caspase-activated DNase (CAD)"),
        PdbEntryInfo(
            "1vas",
            "ATOMIC MODEL OF A PYRIMIDINE DIMER SPECIFIC EXCISION REPAIR ENZYME COMPLEXED WITH A DNA SUBSTRATE: STRUCTURAL BASIS FOR DAMAGED DNA RECOGNITION"
        ),
        PdbEntryInfo("1vf5", "Crystal Structure of Cytochrome b6f Complex from M.laminosus"),
        PdbEntryInfo(
            "1vol",
            "TFIIB (HUMAN CORE DOMAIN)/TBP (A.THALIANA)/TATA ELEMENT TERNARY COMPLEX"
        ),
        PdbEntryInfo(
            "1vpe",
            "CRYSTALLOGRAPHIC ANALYSIS OF PHOSPHOGLYCERATE KINASE FROM THE HYPERTHERMOPHILIC BACTERIUM THERMOTOGA MARITIMA"
        ),
        PdbEntryInfo(
            "1vpr",
            "Crystal structure of a luciferase domain from the dinoflagellate Lingulodinium polyedrum"
        ),
        PdbEntryInfo("1w0e", "Crystal structure of human cytochrome P450 3A4"),
        PdbEntryInfo("1w36", "RecBCD:DNA complex"),
        PdbEntryInfo(
            "1w3b",
            "The superhelical TPR domain of O-linked GlcNAc transferase reveals structural similarities to importin alpha."
        ),
        PdbEntryInfo("1w6k", "Structure of human OSC in complex with Lanosterol"),
        PdbEntryInfo(
            "1w85",
            "The crystal structure of pyruvate dehydrogenase E1 bound to the peripheral subunit binding domain of E2"
        ),
        PdbEntryInfo("1wa5", "Structure of the Cse1:Imp-alpha:RanGTP complex"),
        PdbEntryInfo(
            "1wdw",
            "Structural basis of mutual activation of the tryptophan synthase a2b2 complex from a hyperthermophile, Pyrococcus furiosus"
        ),
        PdbEntryInfo("1wfb", "WINTER FLOUNDER ANTIFREEZE PROTEIN ISOFORM HPLC6 AT-180 DEGREES C"),
        PdbEntryInfo(
            "1wio",
            "STRUCTURE OF T-CELL SURFACE GLYCOPROTEIN CD4, TETRAGONAL CRYSTAL FORM"
        ),
        PdbEntryInfo(
            "1wkw",
            "Crystal structure of the ternary complex of eIF4E-m7GpppA-4EBP1 peptide"
        ),
        PdbEntryInfo("1wq1", "RAS-RASGAP COMPLEX"),
        PdbEntryInfo("1www", "NGF IN COMPLEX WITH DOMAIN 5 OF THE TRKA RECEPTOR"),
        PdbEntryInfo(
            "1x70",
            "HUMAN DIPEPTIDYL PEPTIDASE IV IN COMPLEX WITH A BETA AMINO ACID INHIBITOR"
        ),
        PdbEntryInfo(
            "1xf0",
            "Crystal structure of human 17beta-hydroxysteroid dehydrogenase type 5 (AKR1C3) complexed with delta4-androstene-3,17-dione and NADP"
        ),
        PdbEntryInfo(
            "1xka",
            "FACTOR XA COMPLEXED WITH A SYNTHETIC INHIBITOR FX-2212A,(2S)-(3'-AMIDINO-3-BIPHENYLYL)-5-(4-PYRIDYLAMINO)PENTANOIC ACID"
        ),
        PdbEntryInfo("1xkk", "EGFR kinase domain complexed with a quinazoline inhibitor- GW572016"),
        PdbEntryInfo(
            "1xmb",
            "X-ray structure of IAA-aminoacid hydrolase from Arabidopsis thaliana gene AT5G56660"
        ),
        PdbEntryInfo("1xnj", "APS complex of human PAPS synthetase 1"),
        PdbEntryInfo(
            "1xp0",
            "Catalytic Domain Of Human Phosphodiesterase 5A In Complex With Vardenafil"
        ),
        PdbEntryInfo("1xtc", "CHOLERA TOXIN"),
        PdbEntryInfo(
            "1y0j",
            "Zinc fingers as protein recognition motifs: structural basis for the GATA-1/Friend of GATA interaction"
        ),
        PdbEntryInfo("1y26", "A-riboswitch-adenine complex"),
        PdbEntryInfo("1y27", "G-riboswitch-guanine complex"),
        PdbEntryInfo(
            "1ya5",
            "Crystal structure of the titin domains z1z2 in complex with telethonin"
        ),
        PdbEntryInfo(
            "1ycq",
            "XENOPUS LAEVIS MDM2 BOUND TO THE TRANSACTIVATION DOMAIN OF HUMAN P53"
        ),
        PdbEntryInfo("1ycr", "MDM2 BOUND TO THE TRANSACTIVATION DOMAIN OF P53"),
        PdbEntryInfo("1yet", "GELDANAMYCIN BOUND TO THE HSP90 GELDANAMYCIN-BINDING DOMAIN"),
        PdbEntryInfo("1yfg", "YEAST INITIATOR TRNA"),
        PdbEntryInfo(
            "1ygp",
            "PHOSPHORYLATED FORM OF YEAST GLYCOGEN PHOSPHORYLASE WITH PHOSPHATE BOUND IN THE ACTIVE SITE."
        ),
        PdbEntryInfo(
            "1yhu",
            "Crystal structure of Riftia pachyptila C1 hemoglobin reveals novel assembly of 24 subunits."
        ),
        PdbEntryInfo("1yi5", "Crystal structure of the a-cobratoxin-AChBP complex"),
        PdbEntryInfo("1ykf", "NADP-DEPENDENT ALCOHOL DEHYDROGENASE FROM THERMOANAEROBIUM BROCKII"),
        PdbEntryInfo(
            "1ylv",
            "SCHIFF-BASE COMPLEX OF YEAST 5-AMINOLAEVULINIC ACID DEHYDRATASE WITH LAEVULINIC ACID"
        ),
        PdbEntryInfo(
            "1ymb",
            "HIGH RESOLUTION STUDY OF THE THREE-DIMENSIONAL STRUCTURE OF HORSE HEART METMYOGLOBIN"
        ),
        PdbEntryInfo("1ymg", "The Channel Architecture of Aquaporin O at 2.2 Angstrom Resolution"),
        PdbEntryInfo(
            "1ynw",
            "Crystal Structure of Vitamin D Receptor and 9-cis Retinoic Acid Receptor DNA-Binding Domains Bound to a DR3 Response Element"
        ),
        PdbEntryInfo("1ytb", "CRYSTAL STRUCTURE OF A YEAST TBP/TATA-BOX COMPLEX"),
        PdbEntryInfo("1ytf", "YEAST TFIIA/TBP/DNA COMPLEX"),
        PdbEntryInfo(
            "1yvn",
            "THE YEAST ACTIN VAL 159 ASN MUTANT COMPLEX WITH HUMAN GELSOLIN SEGMENT 1."
        ),
        PdbEntryInfo(
            "1yyf",
            "Correction of X-ray Intensities from an HslV-HslU co-crystal containing lattice translocation defects"
        ),
        PdbEntryInfo(
            "1z1g",
            "Crystal structure of a lambda integrase tetramer bound to a Holliday junction"
        ),
        PdbEntryInfo("1z2b", "Tubulin-colchicine-vinblastine: stathmin-like domain complex"),
        PdbEntryInfo(
            "1z6t",
            "Structure of the apoptotic protease-activating factor 1 bound to ADP"
        ),
        PdbEntryInfo("1z7g", "Free human HGPRT"),
        PdbEntryInfo(
            "1zaa",
            "ZINC FINGER-DNA RECOGNITION: CRYSTAL STRUCTURE OF A ZIF268-DNA COMPLEX AT 2.1 ANGSTROMS"
        ),
        PdbEntryInfo("1zcd", "Crystal structure of the Na+/H+ antiporter NhaA"),
        PdbEntryInfo("1zen", "CLASS II FRUCTOSE-1,6-BISPHOSPHATE ALDOLASE"),
        PdbEntryInfo("1zes", "BeF3- activated PhoB receiver domain"),
        PdbEntryInfo(
            "1znf",
            "THREE-DIMENSIONAL SOLUTION STRUCTURE OF A SINGLE ZINC FINGER DNA-BINDING DOMAIN"
        ),
        PdbEntryInfo(
            "1zqa",
            "DNA POLYMERASE BETA (POL B) (E.C.2.7.7.7) COMPLEXED WITH SEVEN BASE PAIRS OF DNA; SOAKED IN THE PRESENCE OF KCL (150 MILLIMOLAR) AT PH 7.5"
        ),
        PdbEntryInfo("2a1s", "Crystal structure of native PARN nuclease domain"),
        PdbEntryInfo(
            "2a3d",
            "SOLUTION STRUCTURE OF A DE NOVO DESIGNED SINGLE CHAIN THREE-HELIX BUNDLE (A3D)"
        ),
        PdbEntryInfo(
            "2a45",
            "Crystal structure of the complex between thrombin and the central \"E\" region of fibrin"
        ),
        PdbEntryInfo(
            "2a7u",
            "NMR solution structure of the E.coli F-ATPase delta subunit N-terminal domain in complex with alpha subunit N-terminal 22 residues"
        ),
        PdbEntryInfo("2aai", "Crystallographic refinement of ricin to 2.5 Angstroms"),
        PdbEntryInfo(
            "2ahm",
            "Crystal structure of SARS-CoV super complex of non-structural proteins: the hexadecamer"
        ),
        PdbEntryInfo(
            "2am9",
            "Crystal structure of human androgen receptor ligand binding domain in complex with testosterone"
        ),
        PdbEntryInfo(
            "2amb",
            "Crystal structure of human androgen receptor ligand binding domain in complex with tetrahydrogestrinone"
        ),
        PdbEntryInfo(
            "2ayh",
            "CRYSTAL AND MOLECULAR STRUCTURE AT 1.6 ANGSTROMS RESOLUTION OF THE HYBRID BACILLUS ENDO-1,3-1,4-BETA-D-GLUCAN 4-GLUCANOHYDROLASE H(A16-M)"
        ),
        PdbEntryInfo("2az8", "HIV-1 Protease NL4-3 in complex with inhibitor, TL-3"),
        PdbEntryInfo("2az9", "HIV-1 Protease NL4-3 1X mutant"),
        PdbEntryInfo("2azc", "HIV-1 Protease NL4-3 6X mutant"),
        PdbEntryInfo(
            "2b3y",
            "Structure of a monoclinic crystal form of human cytosolic aconitase (IRP1)"
        ),
        PdbEntryInfo("2b4n", "Solution Structure of Glucose-Dependent Insulinotropic Polypeptide"),
        PdbEntryInfo(
            "2bat",
            "THE STRUCTURE OF THE COMPLEX BETWEEN INFLUENZA VIRUS NEURAMINIDASE AND SIALIC ACID, THE VIRAL RECEPTOR"
        ),
        PdbEntryInfo(
            "2bbm",
            "SOLUTION STRUCTURE OF A CALMODULIN-TARGET PEPTIDE COMPLEX BY MULTIDIMENSIONAL NMR"
        ),
        PdbEntryInfo("2beg", "3D Structure of Alzheimer's Abeta(1-42) fibrils"),
        PdbEntryInfo(
            "2bg9",
            "REFINED STRUCTURE OF THE NICOTINIC ACETYLCHOLINE RECEPTOR AT 4A RESOLUTION."
        ),
        PdbEntryInfo(
            "2biw",
            "Crystal structure of apocarotenoid cleavage oxygenase from Synechocystis, native enzyme"
        ),
        PdbEntryInfo("2bku", "Kap95p:RanGTP complex"),
        PdbEntryInfo(
            "2brz",
            "SOLUTION NMR STRUCTURE OF THE SWEET PROTEIN BRAZZEIN, MINIMIZED AVERAGE STRUCTURE"
        ),
        PdbEntryInfo("2buk", "SATELLITE TOBACCO NECROSIS VIRUS"),
        PdbEntryInfo(
            "2c2a",
            "Structure of the entire cytoplasmic portion of a sensor histidine kinase protein"
        ),
        PdbEntryInfo("2cag", "CATALASE COMPOUND II"),
        PdbEntryInfo("2cas", "THE CANINE PARVOVIRUS EMPTY CAPSID STRUCTURE"),
        PdbEntryInfo(
            "2cbj",
            "Structure of the Clostridium perfringens NagJ family 84 glycoside hydrolase, a homologue of human O-GlcNAcase in complex with PUGNAc"
        ),
        PdbEntryInfo("2cf2", "Architecture of mammalian fatty acid synthase"),
        PdbEntryInfo("2cg9", "Crystal structure of an Hsp90-Sba1 closed chaperone complex"),
        PdbEntryInfo(
            "2cha",
            "THE STRUCTURE OF CRYSTALLINE ALPHA-CHYMOTRYPSIN. THE ATOMIC STRUCTURE OF TOSYL-ALPHA-CHYMOTRYPSIN AT 2 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo("2ckb", "STRUCTURE OF THE 2C/KB/DEV8 COMPLEX"),
        PdbEntryInfo(
            "2cpk",
            "CRYSTAL STRUCTURE OF THE CATALYTIC SUBUNIT OF CYCLIC ADENOSINE MONOPHOSPHATE-DEPENDENT PROTEIN KINASE"
        ),
        PdbEntryInfo(
            "2crd",
            "ANALYSIS OF SIDE-CHAIN ORGANIZATION ON A REFINED MODEL OF CHARYBDOTOXIN: STRUCTURAL AND FUNCTIONAL IMPLICATIONS"
        ),
        PdbEntryInfo(
            "2cts",
            "CRYSTALLOGRAPHIC REFINEMENT AND ATOMIC MODELS OF TWO DIFFERENT FORMS OF CITRATE SYNTHASE AT 2.7 AND 1.7 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "2d04",
            "Crystal structure of neoculin, a sweet protein with taste-modifying activity."
        ),
        PdbEntryInfo(
            "2d1s",
            "Crystal structure of the thermostable Japanese Firefly Luciferase complexed with High-energy intermediate analogue"
        ),
        PdbEntryInfo(
            "2d1t",
            "Crystal structure of the thermostable Japanese Firefly Luciferase red-color emission S286N mutant complexed with High-energy intermediate analogue"
        ),
        PdbEntryInfo("2d81", "PHB depolymerase (S39A) complexed with R3HB trimer"),
        PdbEntryInfo(
            "2dcg",
            "MOLECULAR STRUCTURE OF A LEFT-HANDED DOUBLE HELICAL DNA FRAGMENT AT ATOMIC RESOLUTION"
        ),
        PdbEntryInfo("2dez", "Structure of human PYY"),
        PdbEntryInfo(
            "2dhb",
            "THREE DIMENSIONAL FOURIER SYNTHESIS OF HORSE DEOXYHAEMOGLOBIN AT 2.8 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "2dhc",
            "CRYSTALLOGRAPHIC ANALYSIS OF THE CATALYTIC MECHANISM OF HALOALKANE DEHALOGENASE"
        ),
        PdbEntryInfo(
            "2dln",
            "VANCOMYCIN RESISTANCE: STRUCTURE OF D-ALANINE:D-ALANINE LIGASE AT 2.3 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "2drd",
            "Crystal structure of a multidrug transporter reveal a functionally rotating mechanism"
        ),
        PdbEntryInfo(
            "2e4z",
            "Crystal structure of the ligand-binding region of the group III metabotropic glutamate receptor"
        ),
        PdbEntryInfo(
            "2ebt",
            "Solution structure of three tandem repeats of zf-C2H2 domains from human Kruppel-like factor 5"
        ),
        PdbEntryInfo(
            "2eq7",
            "Crystal structure of lipoamide dehydrogenase from thermus thermophilus HB8 with psbdo"
        ),
        PdbEntryInfo(
            "2eud",
            "Structures of Yeast Ribonucleotide Reductase I complexed with Ligands and Subunit Peptides"
        ),
        PdbEntryInfo(
            "2euf",
            "X-ray structure of human CDK6-Vcyclin in complex with the inhibitor PD0332991"
        ),
        PdbEntryInfo(
            "2ez6",
            "Crystal structure of Aquifex aeolicus RNase III (D44N) complexed with product of double-stranded RNA processing"
        ),
        PdbEntryInfo(
            "2ezo",
            "SOLUTION NMR STRUCTURE OF ECTODOMAIN OF SIV GP41, RESTRAINED REGULARIZED MEAN STRUCTURE"
        ),
        PdbEntryInfo(
            "2f1m",
            "Conformational flexibility in the multidrug efflux system protein AcrA"
        ),
        PdbEntryInfo("2f8s", "Crystal structure of Aa-Ago with externally-bound siRNA"),
        PdbEntryInfo("2fae", "Crystal structure of E. coli decanoyl-ACP"),
        PdbEntryInfo("2ffl", "Crystal Structure of Dicer from Giardia intestinalis"),
        PdbEntryInfo("2fk6", "Crystal Structure of RNAse Z/tRNA(Thr) complex"),
        PdbEntryInfo(
            "2fp4",
            "Crystal structure of pig GTP-specific succinyl-CoA synthetase in complex with GTP"
        ),
        PdbEntryInfo("2frv", "CRYSTAL STRUCTURE OF THE OXIDIZED FORM OF NI-FE HYDROGENASE"),
        PdbEntryInfo(
            "2g19",
            "Cellular Oxygen Sensing: Crystal Structure of Hypoxia-Inducible Factor Prolyl Hydroxylase (PHD2)"
        ),
        PdbEntryInfo(
            "2g1m",
            "Cellular Oxygen Sensing: Crystal Structure of Hypoxia-Inducible Factor Prolyl Hydroxylase (PHD2)"
        ),
        PdbEntryInfo("2g30", "beta appendage of AP2 complexed with ARH peptide"),
        PdbEntryInfo(
            "2gbl",
            "Crystal Structure of Full Length Circadian Clock Protein KaiC with Phosphorylation Sites"
        ),
        PdbEntryInfo("2gfp", "Structure of the Multidrug Transporter EmrD from Escherichia coli"),
        PdbEntryInfo("2gg4", "CP4 EPSP synthase (unliganded)"),
        PdbEntryInfo("2gga", "CP4 EPSP synthase liganded with S3P and Glyphosate"),
        PdbEntryInfo("2ggd", "CP4 EPSP synthase Ala100Gly liganded with S3P and Glyphosate"),
        PdbEntryInfo(
            "2ghw",
            "Crystal structure of SARS spike protein receptor binding domain in complex with a neutralizing antibody, 80R"
        ),
        PdbEntryInfo(
            "2gls",
            "REFINED ATOMIC MODEL OF GLUTAMINE SYNTHETASE AT 3.5 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "2gs6",
            "Crystal Structure of the active EGFR kinase domain in complex with an ATP analog-peptide conjugate"
        ),
        PdbEntryInfo("2gtl", "Lumbricus Erythrocruorin at 3.5A resolution"),
        PdbEntryInfo("2h4f", "Sir2-p53 peptide-NAD+"),
        PdbEntryInfo("2h59", "Sir2 H116A-deacetylated p53 peptide-3'-o-acetyl ADP ribose"),
        PdbEntryInfo("2h5o", "Crystal structure of mOrange"),
        PdbEntryInfo("2h5q", "Crystal structure of mCherry"),
        PdbEntryInfo(
            "2h7d",
            "Solution structure of the talin F3 domain in complex with a chimeric beta3 integrin-PIP kinase peptide"
        ),
        PdbEntryInfo(
            "2h9r",
            "Docking and dimerization domain (D/D) of the regulatory subunit of the Type II-alpha cAMP-dependent protein kinase A associated with a Peptide derived from an A-kinase anchoring protein (AKAP)"
        ),
        PdbEntryInfo("2hac", "Structure of Zeta-Zeta Transmembrane Dimer"),
        PdbEntryInfo("2hbs", "THE HIGH RESOLUTION CRYSTAL STRUCTURE OF DEOXYHEMOGLOBIN S"),
        PdbEntryInfo("2hck", "SRC FAMILY KINASE HCK-QUERCETIN COMPLEX"),
        PdbEntryInfo(
            "2hco",
            "THE STRUCTURE OF HUMAN CARBONMONOXY HAEMOGLOBIN AT 2.7 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "2hft",
            "THE CRYSTAL STRUCTURE OF THE EXTRACELLULAR DOMAIN OF HUMAN TISSUE FACTOR AT 1.7 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "2hgh",
            "Transcription Factor IIIA zinc fingers 4-6 bound to 5S rRNA 55mer (NMR structure)"
        ),
        PdbEntryInfo("2hgt", "STRUCTURE OF THE HIRUGEN AND HIRULOG 1 COMPLEXES OF ALPHA-THROMBIN"),
        PdbEntryInfo(
            "2hhb",
            "THE CRYSTAL STRUCTURE OF HUMAN DEOXYHAEMOGLOBIN AT 1.74 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "2hil",
            "Structure of the Neisseria gonorrhoeae Type IV pilus filament from x-ray crystallography and electron cryomicroscopy"
        ),
        PdbEntryInfo(
            "2hiu",
            "NMR STRUCTURE OF HUMAN INSULIN IN 20% ACETIC ACID, ZINC-FREE, 10 STRUCTURES"
        ),
        PdbEntryInfo(
            "2hla",
            "SPECIFICITY POCKETS FOR THE SIDE CHAINS OF PEPTIDE ANTIGENS IN HLA-AW68"
        ),
        PdbEntryInfo("2hmi", "HIV-1 REVERSE TRANSCRIPTASE/FRAGMENT OF FAB 28/DNA COMPLEX"),
        PdbEntryInfo("2hu4", "N1 neuraminidase in complex with oseltamivir 2"),
        PdbEntryInfo("2i8b", "Crystal structure of the C-terminal domain of Ebola virus VP30"),
        PdbEntryInfo("2ic8", "Crystal structure of GlpG"),
        PdbEntryInfo("2ifq", "Crystal structure of S-nitroso thioredoxin"),
        PdbEntryInfo("2ioq", "Crystal Structure of full-length HTPG, the Escherichia coli HSP90"),
        PdbEntryInfo("2irv", "Crystal structure of GlpG, a rhomboid intramembrane serine protease"),
        PdbEntryInfo("2j0d", "Crystal structure of human P450 3A4 in complex with erythromycin"),
        PdbEntryInfo(
            "2j1u",
            "Structure of a Streptococcus pneumoniae fucose binding module in complex with the blood group A-tetrasaccharide"
        ),
        PdbEntryInfo("2j67", "The TIR domain of human Toll-Like Receptor 10 (TLR10)"),
        PdbEntryInfo(
            "2j7w",
            "Dengue virus NS5 RNA dependent RNA polymerase domain complexed with 3' dGTP"
        ),
        PdbEntryInfo("2jgd", "E. COLI 2-oxoglutarate dehydrogenase (E1o)"),
        PdbEntryInfo("2jho", "Cyanomet Sperm Whale Myoglobin at 1.4A resolution"),
        PdbEntryInfo(
            "2jlb",
            "Xanthomonas campestris putative OGT (XCC0866), complex with UDP- GlcNAc phosphonate analogue"
        ),
        PdbEntryInfo(
            "2jzq",
            "Design of an Active Ultra-Stable Single-Chain Insulin Analog 20 Structures"
        ),
        PdbEntryInfo("2k6o", "Human LL-37 Structure"),
        PdbEntryInfo("2k9j", "Integrin alphaIIb-beta3 transmembrane complex"),
        PdbEntryInfo("2ka6", "NMR structure of the CBP-TAZ2/STAT1-TAD complex"),
        PdbEntryInfo("2kh2", "Solution structure of a scFv-IL-1B complex"),
        PdbEntryInfo("2kin", "KINESIN (MONOMERIC) FROM RATTUS NORVEGICUS"),
        PdbEntryInfo(
            "2kj3",
            "High-resolution structure of the HET-s(218-289) prion in its amyloid form obtained by solid-state NMR"
        ),
        PdbEntryInfo(
            "2kod",
            "A high-resolution NMR structure of the dimeric C-terminal domain of HIV-1 CA"
        ),
        PdbEntryInfo(
            "2kz1",
            "Inter-molecular interactions in a 44 kDa interferon-receptor complex detected by asymmetric back-protonation and 2D NOESY"
        ),
        PdbEntryInfo("2l63", "NMR solution structure of GLP-2 in 2,2,2 trifluroethanol"),
        PdbEntryInfo("2l7u", "Structure of CEL-PEP-RAGE V domain complex"),
        PdbEntryInfo(
            "2lhb",
            "REFINEMENT OF A MOLECULAR MODEL FOR LAMPREY HEMOGLOBIN FROM PETROMYZON MARINUS"
        ),
        PdbEntryInfo("2lm3", "Structure of the rhesus monkey TRIM5alpha PRYSPRY domain"),
        PdbEntryInfo(
            "2lmn",
            "Structural Model for a 40-Residue Beta-Amyloid Fibril with Two-Fold Symmetry, Positive Stagger"
        ),
        PdbEntryInfo(
            "2lmp",
            "Structural Model for a 40-residue Beta-Amyloid Fibril with Three-Fold Symmetry, Positive Stagger"
        ),
        PdbEntryInfo("2lyz", "Real-space refinement of the structure of hen egg-white lysozyme"),
        PdbEntryInfo(
            "2m4j",
            "40-residue beta-amyloid fibril derived from Alzheimer's disease brain"
        ),
        PdbEntryInfo(
            "2mfr",
            "Solution structure of the transmembrane domain of the insulin receptor in micelles"
        ),
        PdbEntryInfo(
            "2mys",
            "MYOSIN SUBFRAGMENT-1, ALPHA CARBON COORDINATES ONLY FOR THE TWO LIGHT CHAINS"
        ),
        PdbEntryInfo(
            "2n5e",
            "The 3D solution structure of discoidal high-density lipoprotein particles"
        ),
        PdbEntryInfo("2ncd", "NCD (NON-CLARET DISJUNCTIONAL) DIMER FROM D. MELANOGASTER"),
        PdbEntryInfo(
            "2nll",
            "RETINOID X RECEPTOR-THYROID HORMONE RECEPTOR DNA-BINDING DOMAIN HETERODIMER BOUND TO THYROID RESPONSE ELEMENT DNA"
        ),
        PdbEntryInfo(
            "2nn6",
            "Structure of the human RNA exosome composed of Rrp41, Rrp45, Rrp46, Rrp43, Mtr3, Rrp42, Csl4, Rrp4, and Rrp40"
        ),
        PdbEntryInfo("2nrf", "Crystal Structure of GlpG, a Rhomboid family intramembrane protease"),
        PdbEntryInfo("2nrl", "Blackfin tuna myoglobin"),
        PdbEntryInfo("2nrm", "S-nitrosylated blackfin tuna myoglobin"),
        PdbEntryInfo("2nse", "BOVINE ENDOTHELIAL NITRIC OXIDE SYNTHASE SUBSTRATE COMPLEX"),
        PdbEntryInfo(
            "2nwx",
            "Crystal structure of GltPh in complex with L-aspartate and sodium ions"
        ),
        PdbEntryInfo(
            "2o0c",
            "Crystal structure of the H-NOX domain from Nostoc sp. PCC 7120 complexed to NO"
        ),
        PdbEntryInfo("2o1u", "Structure of full length GRP94 with AMP-PNP bound"),
        PdbEntryInfo("2o1v", "Structure of full length GRP94 with ADP bound"),
        PdbEntryInfo(
            "2o39",
            "Human Adenovirus type 11 knob in complex with domains SCR1 and SCR2 of CD46 (membrane cofactor protein, MCP)"
        ),
        PdbEntryInfo("2o60", "Calmodulin bound to peptide from neuronal nitric oxide synthase"),
        PdbEntryInfo(
            "2o61",
            "Crystal Structure of NFkB, IRF7, IRF3 bound to the interferon-b enhancer"
        ),
        PdbEntryInfo("2o6g", "Crystal structure of IRF-3 bound to the interferon-b enhancer"),
        PdbEntryInfo("2oar", "Mechanosensitive Channel of Large Conductance (MscL)"),
        PdbEntryInfo("2oau", "Mechanosensitive Channel of Small Conductance (MscS)"),
        PdbEntryInfo(
            "2obs",
            "Crystal Structures of P Domain of Norovirus VA387 in Complex with Blood Group Trisaccharides type A"
        ),
        PdbEntryInfo("2om3", "High-resolution cryo-EM structure of Tobacco Mosaic Virus"),
        PdbEntryInfo(
            "2one",
            "ASYMMETRIC YEAST ENOLASE DIMER COMPLEXED WITH RESOLVED 2'-PHOSPHOGLYCERATE AND PHOSPHOENOLPYRUVATE"
        ),
        PdbEntryInfo(
            "2onj",
            "Structure of the multidrug ABC transporter Sav1866 from S. aureus in complex with AMP-PNP"
        ),
        PdbEntryInfo("2oq1", "Tandem SH2 domains of ZAP-70 with 19-mer zeta1 peptide"),
        PdbEntryInfo(
            "2or1",
            "RECOGNITION OF A DNA OPERATOR BY THE REPRESSOR OF PHAGE 434. A VIEW AT HIGH RESOLUTION"
        ),
        PdbEntryInfo("2ozo", "Autoinhibited intact human ZAP-70"),
        PdbEntryInfo(
            "2p04",
            "2.1 Ang structure of the dimerized PAS domain of signal transduction histidine kinase from Nostoc punctiforme PCC 73102 with homology to the H-NOXA/H-NOBA domain of the soluble guanylyl cyclase"
        ),
        PdbEntryInfo("2p1h", "Rapid Folding and Unfolding of Apaf-1 CARD"),
        PdbEntryInfo("2p1n", "Mechanism of Auxin Perception by the TIR1 Ubiqutin Ligase"),
        PdbEntryInfo("2p1p", "Mechanism of Auxin Perception by the TIR1 ubiquitin ligase"),
        PdbEntryInfo("2p1q", "Mechanism of Auxin Perception by the TIR1 ubiquitin ligase"),
        PdbEntryInfo("2pah", "TETRAMERIC HUMAN PHENYLALANINE HYDROXYLASE"),
        PdbEntryInfo(
            "2pd7",
            "2.0 Angstrom Crystal Structure of the Fungal Blue-Light Photoreceptor Vivid"
        ),
        PdbEntryInfo("2pel", "PEANUT LECTIN"),
        PdbEntryInfo(
            "2pf2",
            "THE CA+2 ION AND MEMBRANE BINDING STRUCTURE OF THE GLA DOMAIN OF CA-PROTHROMBIN FRAGMENT 1"
        ),
        PdbEntryInfo(
            "2pgi",
            "THE CRYSTAL STRUCTURE OF PHOSPHOGLUCOSE ISOMERASE-AN ENZYME WITH AUTOCRINE MOTILITY FACTOR ACTIVITY IN TUMOR CELLS"
        ),
        PdbEntryInfo(
            "2pi0",
            "Crystal Structure of IRF-3 bound to the PRDIII-I regulatory element of the human interferon-B enhancer"
        ),
        PdbEntryInfo(
            "2plv",
            "STRUCTURAL FACTORS THAT CONTROL CONFORMATIONAL TRANSITIONS AND SEROTYPE SPECIFICITY IN TYPE 3 POLIOVIRUS"
        ),
        PdbEntryInfo("2pne", "Crystal Structure of the Snow Flea Antifreeze Protein"),
        PdbEntryInfo(
            "2pqb",
            "CP4 EPSPS liganded with (R)-difluoromethyl tetrahedral intermediate analog"
        ),
        PdbEntryInfo(
            "2ptc",
            "THE GEOMETRY OF THE REACTIVE SITE AND OF THE PEPTIDE GROUPS IN TRYPSIN, TRYPSINOGEN AND ITS COMPLEXES WITH INHIBITORS"
        ),
        PdbEntryInfo(
            "2ptn",
            "ON THE DISORDERED ACTIVATION DOMAIN IN TRYPSINOGEN. CHEMICAL LABELLING AND LOW-TEMPERATURE CRYSTALLOGRAPHY"
        ),
        PdbEntryInfo(
            "2pyp",
            "PHOTOACTIVE YELLOW PROTEIN, PHOTOSTATIONARY STATE, 50% GROUND STATE, 50% BLEACHED"
        ),
        PdbEntryInfo(
            "2q3z",
            "Transglutaminase 2 undergoes large conformational change upon activation"
        ),
        PdbEntryInfo(
            "2q57",
            "X-ray structure of Cerulean GFP: A tryptophan-based chromophore useful for fluorescence lifetime imaging"
        ),
        PdbEntryInfo("2q66", "Structure of Yeast Poly(A) Polymerase with ATP and oligo(A)"),
        PdbEntryInfo("2qbz", "Structure of the M-Box Riboswitch Aptamer Domain"),
        PdbEntryInfo(
            "2qkm",
            "The crystal structure of fission yeast mRNA decapping enzyme Dcp1-Dcp2 complex"
        ),
        PdbEntryInfo("2qrv", "Structure of Dnmt3a-Dnmt3L C-terminal domain complex"),
        PdbEntryInfo("2qw7", "Carboxysome Subunit, CcmL"),
        PdbEntryInfo("2r4r", "Crystal structure of the human beta2 adrenoceptor"),
        PdbEntryInfo(
            "2r6p",
            "Fit of E protein and Fab 1A1D-2 into 24 angstrom resolution cryoEM map of Fab complexed with dengue 2 virus."
        ),
        PdbEntryInfo(
            "2rh1",
            "High resolution crystal structure of human B2-adrenergic G protein-coupled receptor."
        ),
        PdbEntryInfo("2rik", "I-band fragment I67-I69 from titin"),
        PdbEntryInfo(
            "2rnm",
            "Structure of The HET-s(218-289) prion in its amyloid form obtained by solid-state NMR"
        ),
        PdbEntryInfo(
            "2sod",
            "DETERMINATION AND ANALYSIS OF THE 2 ANGSTROM STRUCTURE OF COPPER, ZINC SUPEROXIDE DISMUTASE"
        ),
        PdbEntryInfo(
            "2src",
            "CRYSTAL STRUCTURE OF HUMAN TYROSINE-PROTEIN KINASE C-SRC, IN COMPLEX WITH AMP-PNP"
        ),
        PdbEntryInfo("2taa", "STRUCTURE AND POSSIBLE CATALYTIC RESIDUES OF TAKA-AMYLASE A"),
        PdbEntryInfo(
            "2tmv",
            "VISUALIZATION OF PROTEIN-NUCLEIC ACID INTERACTIONS IN A VIRUS. REFINED STRUCTURE OF INTACT TOBACCO MOSAIC VIRUS AT 2.9 ANGSTROMS RESOLUTION BY X-RAY FIBER DIFFRACTION"
        ),
        PdbEntryInfo("2toh", "TYROSINE HYDROXYLASE CATALYTIC AND TETRAMERIZATION DOMAINS FROM RAT"),
        PdbEntryInfo(
            "2tra",
            "RESTRAINED REFINEMENT OF TWO CRYSTALLINE FORMS OF YEAST ASPARTIC ACID AND PHENYLALANINE TRANSFER RNA CRYSTALS"
        ),
        PdbEntryInfo(
            "2uwm",
            "C-TERMINAL DOMAIN(WH2-WH4) OF ELONGATION FACTOR SELB IN COMPLEX WITH SECIS RNA"
        ),
        PdbEntryInfo("2v01", "Recombinant vertebrate calmodulin complexed with Pb"),
        PdbEntryInfo(
            "2vaa",
            "MHC CLASS I H-2KB HEAVY CHAIN COMPLEXED WITH BETA-2 MICROGLOBULIN AND VESICULAR STOMATITIS VIRUS NUCLEOPROTEIN"
        ),
        PdbEntryInfo(
            "2vab",
            "MHC CLASS I H-2KB HEAVY CHAIN COMPLEXED WITH BETA-2 MICROGLOBULIN AND SENDAI VIRUS NUCLEOPROTEIN"
        ),
        PdbEntryInfo("2vbc", "Crystal structure of the NS3 protease-helicase from Dengue virus"),
        PdbEntryInfo(
            "2vdo",
            "Integrin AlphaIIbBeta3 Headpiece Bound to Fibrinogen Gamma chain peptide, HHLGGAKQAGDV"
        ),
        PdbEntryInfo(
            "2vir",
            "INFLUENZA VIRUS HEMAGGLUTININ COMPLEXED WITH A NEUTRALIZING ANTIBODY"
        ),
        PdbEntryInfo("2vv5", "The open structure of MscS"),
        PdbEntryInfo("2wj7", "human alphaB crystallin"),
        PdbEntryInfo(
            "2xow",
            "Structure of GlpG in complex with a mechanism-based isocoumarin inhibitor"
        ),
        PdbEntryInfo("2y0g", "X-ray structure of Enhanced Green Fluorescent Protein (EGFP)"),
        PdbEntryInfo(
            "2yhx",
            "SEQUENCING A PROTEIN BY X-RAY CRYSTALLOGRAPHY. II. REFINEMENT OF YEAST HEXOKINASE B CO-ORDINATES AND SEQUENCE AT 2.1 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "2ymk",
            "Crystal structure of the hexameric anti-microbial peptide channel dermcidin"
        ),
        PdbEntryInfo(
            "2ypi",
            "CRYSTALLOGRAPHIC ANALYSIS OF THE COMPLEX BETWEEN TRIOSEPHOSPHATE ISOMERASE AND 2-PHOSPHOGLYCOLATE AT 2.5-ANGSTROMS RESOLUTION. IMPLICATIONS FOR CATALYSIS"
        ),
        PdbEntryInfo(
            "2yye",
            "Crystal structure of selenophosphate synthetase from Aquifex aeolicus complexed with AMPCPP"
        ),
        PdbEntryInfo(
            "2z6c",
            "Crystal structure of LOV1 domain of phototropin1 from Arabidopsis thaliana"
        ),
        PdbEntryInfo("2z75", "T. tengcongensis glmS ribozyme bound to glucosamine-6-phosphate"),
        PdbEntryInfo(
            "2z7x",
            "Crystal structure of the TLR1-TLR2 heterodimer induced by binding of a tri-acylated lipopeptide"
        ),
        PdbEntryInfo(
            "2zb5",
            "Crystal structure of the measles virus hemagglutinin (complex-sugar-type)"
        ),
        PdbEntryInfo(
            "2zib",
            "Crystal structure analysis of calcium-independent type II antifreeze protein"
        ),
        PdbEntryInfo(
            "2zoi",
            "Neutron Crystal Structure of Photoactive Yellow Protein, Wild type, at 295K"
        ),
        PdbEntryInfo(
            "2zta",
            "X-RAY STRUCTURE OF THE GCN4 LEUCINE ZIPPER, A TWO-STRANDED, PARALLEL COILED COIL"
        ),
        PdbEntryInfo(
            "2zxe",
            "Crystal structure of the sodium - potassium pump in the E2.2K+.Pi state"
        ),
        PdbEntryInfo(
            "2zyv",
            "Crystal structure of mouse cytosolic sulfotransferase mSULT1D1 complex with PAPS/PAP and p-nitrophenol"
        ),
        PdbEntryInfo(
            "309d",
            "A DNA DECAMER WITH A STICKY END: THE CRYSTAL STRUCTURE OF D-CGACGATCGT"
        ),
        PdbEntryInfo(
            "3a3y",
            "Crystal structure of the sodium-potassium pump with bound potassium and ouabain"
        ),
        PdbEntryInfo("3ado", "Crystal Structure of the Rabbit L-Gulonate 3-Dehydrogenase"),
        PdbEntryInfo(
            "3alz",
            "Crystal structure of the measles virus hemagglutinin bound to its cellular receptor SLAM (Form I)"
        ),
        PdbEntryInfo(
            "3amr",
            "Crystal Structures of Bacillus subtilis Alkaline Phytase in Complex with Ca2+, Co2+, Ni2+, Mg2+ and myo-Inositol Hexasulfate"
        ),
        PdbEntryInfo("3b43", "I-band fragment I65-I70 from titin"),
        PdbEntryInfo("3b4r", "Site-2 Protease from Methanocaldococcus jannaschii"),
        PdbEntryInfo("3b75", "Crystal Structure of Glycated Human Haemoglobin"),
        PdbEntryInfo(
            "3b7e",
            "Neuraminidase of A/Brevig Mission/1/1918 H1N1 strain in complex with zanamivir"
        ),
        PdbEntryInfo("3b8e", "Crystal structure of the sodium-potassium pump"),
        PdbEntryInfo("3bc8", "Crystal structure of mouse selenocysteine synthase"),
        PdbEntryInfo("3bes", "Structure of a Poxvirus ifngbp/ifng Complex"),
        PdbEntryInfo(
            "3bgf",
            "X-ray crystal structure of the SARS coronavirus spike receptor binding domain in complex with F26G19 Fab"
        ),
        PdbEntryInfo("3bik", "Crystal Structure of the PD-1/PD-L1 Complex"),
        PdbEntryInfo(
            "3biy",
            "Crystal structure of p300 histone acetyltransferase domain in complex with a bisubstrate inhibitor, Lys-CoA"
        ),
        PdbEntryInfo(
            "3blw",
            "Yeast Isocitrate Dehydrogenase with Citrate and AMP Bound in the Regulatory Subunits"
        ),
        PdbEntryInfo("3bn4", "Carboxysome Subunit, CcmK1"),
        PdbEntryInfo("3bp5", "Crystal structure of the mouse PD-1 and PD-L2 complex"),
        PdbEntryInfo("3by8", "Crystal Structure of the E.coli DcuS Sensor Domain"),
        PdbEntryInfo("3c6g", "Crystal structure of CYP2R1 in complex with vitamin D3"),
        PdbEntryInfo(
            "3cap",
            "Crystal Structure of Native Opsin: the G Protein-Coupled Receptor Rhodopsin in its Ligand-free State"
        ),
        PdbEntryInfo(
            "3ciy",
            "Mouse Toll-like receptor 3 ectodomain complexed with double-stranded RNA"
        ),
        PdbEntryInfo("3cl0", "N1 Neuraminidase H274Y + oseltamivir"),
        PdbEntryInfo("3cln", "STRUCTURE OF CALMODULIN REFINED AT 2.2 ANGSTROMS RESOLUTION"),
        PdbEntryInfo(
            "3cmp",
            "Crystal structure of Siderocalin (NGAL, Lipocalin 2) K125A mutant complexed with Ferric Enterobactin"
        ),
        PdbEntryInfo(
            "3cmx",
            "Mechanism of homologous recombination from the RecA-ssDNA/dsDNA structures"
        ),
        PdbEntryInfo("3cna", "STRUCTURE OF CONCANAVALIN A AT 2.4 ANGSTROMS RESOLUTION"),
        PdbEntryInfo(
            "3cpa",
            "X-RAY CRYSTALLOGRAPHIC INVESTIGATION OF SUBSTRATE BINDING TO CARBOXYPEPTIDASE A AT SUBZERO TEMPERATURE"
        ),
        PdbEntryInfo(
            "3cpp",
            "CRYSTAL STRUCTURE OF THE CARBON MONOXY-SUBSTRATE-CYTOCHROME P450-CAM TERNARY COMPLEX"
        ),
        PdbEntryInfo(
            "3csh",
            "Crystal Structure of Glutathione Transferase Pi in complex with the Chlorambucil-Glutathione Conjugate"
        ),
        PdbEntryInfo(
            "3csy",
            "Crystal structure of the trimeric prefusion Ebola virus glycoprotein in complex with a neutralizing antibody from a human survivor"
        ),
        PdbEntryInfo("3cyt", "REDOX CONFORMATION CHANGES IN REFINED TUNA CYTOCHROME C"),
        PdbEntryInfo(
            "3cyu",
            "Human Carbonic Anhydrase II complexed with Cryptophane biosensor and xenon"
        ),
        PdbEntryInfo(
            "3d6n",
            "Crystal Structure of Aquifex Dihydroorotase Activated by Aspartate Transcarbamoylase"
        ),
        PdbEntryInfo(
            "3dag",
            "The crystal structure of [Fe]-hydrogenase holoenzyme (HMD) from METHANOCALDOCOCCUS JANNASCHII"
        ),
        PdbEntryInfo(
            "3dfr",
            "CRYSTAL STRUCTURES OF ESCHERICHIA COLI AND LACTOBACILLUS CASEI DIHYDROFOLATE REDUCTASE REFINED AT 1.7 ANGSTROMS RESOLUTION. I. GENERAL FEATURES AND BINDING OF METHOTREXATE"
        ),
        PdbEntryInfo(
            "3dge",
            "Structure of a histidine kinase-response regulator complex reveals insights into Two-component signaling and a novel cis-autophosphorylation mechanism"
        ),
        PdbEntryInfo("3dkt", "Crystal structure of Thermotoga maritima encapsulin"),
        PdbEntryInfo("3e7t", "Structure of murine iNOS oxygenase domain with inhibitor AR-C102222"),
        PdbEntryInfo(
            "3ert",
            "HUMAN ESTROGEN RECEPTOR ALPHA LIGAND-BINDING DOMAIN IN COMPLEX WITH 4-HYDROXYTAMOXIFEN"
        ),
        PdbEntryInfo(
            "3est",
            "STRUCTURE OF NATIVE PORCINE PANCREATIC ELASTASE AT 1.65 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "3et6",
            "The crystal structure of the catalytic domain of a eukaryotic guanylate cyclase"
        ),
        PdbEntryInfo(
            "3ets",
            "Crystal structure of a bacterial arylsulfate sulfotransferase catalytic intermediate with 4-methylumbelliferone bound in the active site"
        ),
        PdbEntryInfo("3eub", "Crystal Structure of Desulfo-Xanthine Oxidase with Xanthine"),
        PdbEntryInfo(
            "3eyc",
            "New crystal structure of human tear lipocalin in complex with 1,4-butanediol in space group P21"
        ),
        PdbEntryInfo("3f3e", "Crystal structure of LeuT bound to L-leucine (30 mM) and sodium"),
        PdbEntryInfo(
            "3f47",
            "The Crystal Structure of [Fe]-Hydrogenase (Hmd) Holoenzyme from Methanocaldococcus jannaschii"
        ),
        PdbEntryInfo("3fke", "Structure of the Ebola VP35 Interferon Inhibitory Domain"),
        PdbEntryInfo("3frh", "Structure of the 16S rRNA methylase RmtB, P21"),
        PdbEntryInfo("3fsn", "Crystal structure of RPE65 at 2.14 angstrom resolution"),
        PdbEntryInfo("3fvy", "Crystal structure of human Dipeptidyl Peptidase III"),
        PdbEntryInfo(
            "3fw4",
            "Crystal structure of Siderocalin (NGAL, Lipocalin 2) complexed with Ferric Catechol"
        ),
        PdbEntryInfo(
            "3fxi",
            "Crystal structure of the human TLR4-human MD-2-E.coli LPS Ra complex"
        ),
        PdbEntryInfo(
            "3g60",
            "Structure of P-glycoprotein Reveals a Molecular Basis for Poly-Specific Drug Binding"
        ),
        PdbEntryInfo(
            "3g61",
            "Structure of P-glycoprotein Reveals a Molecular Basis for Poly-Specific Drug Binding"
        ),
        PdbEntryInfo(
            "3gbi",
            "The Rational Design and Structural Analysis of a Self-Assembled Three-Dimensional DNA Crystal"
        ),
        PdbEntryInfo(
            "3gpd",
            "TWINNING IN CRYSTALS OF HUMAN SKELETAL MUSCLE D-GLYCERALDEHYDE-3-PHOSPHATE DEHYDROGENASE"
        ),
        PdbEntryInfo(
            "3gss",
            "HUMAN GLUTATHIONE S-TRANSFERASE P1-1 IN COMPLEX WITH ETHACRYNIC ACID-GLUTATHIONE CONJUGATE"
        ),
        PdbEntryInfo("3gwv", "Leucine transporter LeuT in complex with R-fluoxetine"),
        PdbEntryInfo("3h1j", "Stigmatellin-bound cytochrome bc1 complex from chicken"),
        PdbEntryInfo("3h47", "X-ray Structure of Hexameric HIV-1 CA"),
        PdbEntryInfo(
            "3hfm",
            "STRUCTURE OF AN ANTIBODY-ANTIGEN COMPLEX. CRYSTAL STRUCTURE OF THE HY/HEL-10 FAB-LYSOZYME COMPLEX"
        ),
        PdbEntryInfo(
            "3hhr",
            "HUMAN GROWTH HORMONE AND EXTRACELLULAR DOMAIN OF ITS RECEPTOR: CRYSTAL STRUCTURE OF THE COMPLEX"
        ),
        PdbEntryInfo(
            "3hls",
            "Crystal structure of the signaling helix coiled-coil doimain of the BETA-1 subunit of the soluble guanylyl cyclase"
        ),
        PdbEntryInfo("3hqr", "PHD2:Mn:NOG:HIF1-alpha substrate complex"),
        PdbEntryInfo(
            "3hvt",
            "STRUCTURAL BASIS OF ASYMMETRY IN THE HUMAN IMMUNODEFICIENCY VIRUS TYPE 1 REVERSE TRANSCRIPTASE HETERODIMER"
        ),
        PdbEntryInfo(
            "3hz3",
            "Lactobacillus reuteri N-terminally truncated glucansucrase GTF180(D1025N)-sucrose complex"
        ),
        PdbEntryInfo("3i0g", "Crystal structure of GTB C80S/C196S + DA + UDP-Gal"),
        PdbEntryInfo(
            "3icd",
            "STRUCTURE OF A BACTERIAL ENZYME REGULATED BY PHOSPHORYLATION, ISOCITRATE DEHYDROGENASE"
        ),
        PdbEntryInfo(
            "3inb",
            "Structure of the measles virus hemagglutinin bound to the CD46 receptor"
        ),
        PdbEntryInfo(
            "3iol",
            "Crystal structure of Glucagon-Like Peptide-1 in complex with the extracellular domain of the Glucagon-Like Peptide-1 Receptor"
        ),
        PdbEntryInfo("3irw", "Structure of a c-di-GMP riboswitch from V. cholerae"),
        PdbEntryInfo("3iwm", "The octameric SARS-CoV main protease"),
        PdbEntryInfo("3iwn", "Co-crystal structure of a bacterial c-di-GMP riboswitch"),
        PdbEntryInfo("3ixz", "Pig gastric H+/K+-ATPase complexed with aluminium fluoride"),
        PdbEntryInfo("3iyq", "tmRNA-SmpB: a journey to the center of the bacterial ribosome"),
        PdbEntryInfo("3iyr", "tmRNA-SmpB: a journey to the center of the bacterial ribosome"),
        PdbEntryInfo(
            "3iz4",
            "Modified E. coli tmRNA in the resume state with the tRNA-like domain in the ribosomal P site interacting with the SmpB"
        ),
        PdbEntryInfo("3j1t", "High affinity dynein microtubule binding domain - tubulin complex"),
        PdbEntryInfo("3j2u", "Kinesin-13 KLP10A HD in complex with CS-tubulin and a microtubule"),
        PdbEntryInfo(
            "3j5m",
            "Cryo-EM structure of the BG505 SOSIP.664 HIV-1 Env trimer with 3 PGV04 Fabs"
        ),
        PdbEntryInfo("3j6r", "Electron cryo-microscopy of Human Papillomavirus Type 16 capsid"),
        PdbEntryInfo("3jac", "Cryo-EM study of a channel"),
        PdbEntryInfo(
            "3jad",
            "Structure of alpha-1 glycine receptor by single particle electron cryo-microscopy, strychnine-bound state"
        ),
        PdbEntryInfo(
            "3kg2",
            "AMPA subtype ionotropic glutamate receptor in complex with competitive antagonist ZK 200775"
        ),
        PdbEntryInfo("3kin", "KINESIN (DIMERIC) FROM RATTUS NORVEGICUS"),
        PdbEntryInfo(
            "3kll",
            "Crystal structure of Lactobacillus reuteri N-terminally truncated glucansucrase GTF180-maltose complex"
        ),
        PdbEntryInfo("3kud", "Complex of Ras-GDP with RafRBD(A85K)"),
        PdbEntryInfo("3kyh", "Saccharomyces cerevisiae Cet1-Ceg1 capping apparatus"),
        PdbEntryInfo("3l1e", "Bovine AlphaA crystallin Zinc Bound"),
        PdbEntryInfo(
            "3lcb",
            "The crystal structure of isocitrate dehydrogenase kinase/phosphatase in complex with its substrate, isocitrate dehydrogenase, from Escherichia coli."
        ),
        PdbEntryInfo(
            "3ldh",
            "A comparison of the structures of apo dogfish m4 lactate dehydrogenase and its ternary complexes"
        ),
        PdbEntryInfo(
            "3lpw",
            "Crystal structure of the FnIII-tandem A77-A78 from the A-band of titin"
        ),
        PdbEntryInfo("3lqq", "Structure of the CED-4 Apoptosome"),
        PdbEntryInfo(
            "3ly6",
            "Crystal structure of human transglutaminase 2 complex with adenosine 5' Triphosphate"
        ),
        PdbEntryInfo("3m24", "Crystal structure of TagBFP fluorescent protein"),
        PdbEntryInfo("3m7r", "Crystal structure of VDR H305Q mutant"),
        PdbEntryInfo(
            "3m9s",
            "Crystal structure of respiratory complex I from Thermus thermophilus"
        ),
        PdbEntryInfo("3mge", "X-ray Structure of Hexameric HIV-1 CA"),
        PdbEntryInfo(
            "3mmj",
            "Structure of the PTP-like phytase from Selenomonas ruminantium in complex with myo-inositol hexakisphosphate"
        ),
        PdbEntryInfo("3mon", "CRYSTAL STRUCTURES OF TWO INTENSELY SWEET PROTEINS"),
        PdbEntryInfo(
            "3n0g",
            "Crystal Structure of Isoprene Synthase from Grey Poplar Leaves (Populus x canescens) in complex with three Mg2+ ions and dimethylallyl-S-thiolodiphosphate"
        ),
        PdbEntryInfo("3nhc", "GYMLGS segment 127-132 from human prion with M129"),
        PdbEntryInfo("3nir", "Crystal structure of small protein crambin at 0.48 A resolution"),
        PdbEntryInfo(
            "3nkx",
            "Impaired binding of 14-3-3 to Raf1 is linked to Noonan and LEOPARD syndrome"
        ),
        PdbEntryInfo("3og7", "B-Raf Kinase V600E oncogenic mutant in complex with PLX4032"),
        PdbEntryInfo("3os0", "PFV strand transfer complex (STC) at 2.81 A resolution"),
        PdbEntryInfo("3os1", "PFV target capture complex (TCC) at 2.97 A resolution"),
        PdbEntryInfo("3p05", "X-ray structure of pentameric HIV-1 CA"),
        PdbEntryInfo(
            "3p5p",
            "Crystal Structure of Taxadiene Synthase from Pacific Yew (Taxus brevifolia) in complex with Mg2+ and 13-aza-13,14-dihydrocopalyl diphosphate"
        ),
        PdbEntryInfo("3pb3", "Structure of an Antibiotic Related Methyltransferase"),
        PdbEntryInfo(
            "3pe4",
            "Structure of human O-GlcNAc transferase and its complex with a peptide substrate"
        ),
        PdbEntryInfo(
            "3pgk",
            "The structure of yeast phosphoglycerate kinase at 0.25 nm resolution"
        ),
        PdbEntryInfo(
            "3pgm",
            "THE STRUCTURE OF YEAST PHOSPHOGLYCERATE MUTASE AT 0.28 NM RESOLUTION"
        ),
        PdbEntryInfo(
            "3pgt",
            "CRYSTAL STRUCTURE OF HGSTP1-1[I104] COMPLEXED WITH THE GSH CONJUGATE OF (+)-ANTI-BPDE"
        ),
        PdbEntryInfo(
            "3pqr",
            "Crystal structure of Metarhodopsin II in complex with a C-terminal peptide derived from the Galpha subunit of transducin"
        ),
        PdbEntryInfo("3psg", "THE HIGH RESOLUTION CRYSTAL STRUCTURE OF PORCINE PEPSINOGEN"),
        PdbEntryInfo("3pt6", "Crystal structure of mouse DNMT1(650-1602) in complex with DNA"),
        PdbEntryInfo(
            "3pte",
            "THE REFINED CRYSTALLOGRAPHIC STRUCTURE OF A DD-PEPTIDASE PENICILLIN-TARGET ENZYME AT 1.6 A RESOLUTION"
        ),
        PdbEntryInfo("3q6e", "Human insulin in complex with cucurbit[7]uril"),
        PdbEntryInfo(
            "3r1k",
            "Crystal structure of acetyltransferase Eis from Mycobacterium tuberculosis H37Rv in complex with CoA and an acetamide moiety"
        ),
        PdbEntryInfo(
            "3r1r",
            "RIBONUCLEOTIDE REDUCTASE R1 PROTEIN WITH AMPPNP OCCUPYING THE ACTIVITY SITE FROM ESCHERICHIA COLI"
        ),
        PdbEntryInfo("3r8f", "Replication initiator DnaA bound to AMPPCP and single-stranded DNA"),
        PdbEntryInfo("3rgk", "Crystal Structure of Human Myoglobin Mutant K45R"),
        PdbEntryInfo(
            "3rh8",
            "Crystal Structure of the Light-state Dimer of Fungal Blue-Light Photoreceptor Vivid"
        ),
        PdbEntryInfo(
            "3rko",
            "Crystal structure of the membrane domain of respiratory complex I from E. coli at 3.0 angstrom resolution"
        ),
        PdbEntryInfo("3rui", "Crystal structure of Atg7C-Atg8 complex"),
        PdbEntryInfo("3rxw", "KPC-2 carbapenemase in complex with PSR3-226"),
        PdbEntryInfo("3s0x", "The crystal structure of GxGD membrane protease FlaK"),
        PdbEntryInfo(
            "3sdp",
            "THE 2.1 ANGSTROMS RESOLUTION STRUCTURE OF IRON SUPEROXIDE DISMUTASE FROM PSEUDOMONAS OVALIS"
        ),
        PdbEntryInfo(
            "3sdy",
            "Crystal Structure of Broadly Neutralizing Antibody CR8020 Bound to the Influenza A H3 Hemagglutinin"
        ),
        PdbEntryInfo("3se7", "ancient VanA"),
        PdbEntryInfo("3sfz", "Crystal structure of full-length murine Apaf-1"),
        PdbEntryInfo("3srp", "Structure of Rivax: A Human Ricin Vaccine"),
        PdbEntryInfo("3tnp", "Structure and Allostery of the PKA RIIb Tetrameric Holoenzyme"),
        PdbEntryInfo(
            "3tom",
            "Crystal structure of an engineered cytochrome cb562 that forms 2D, Zn-mediated sheets"
        ),
        PdbEntryInfo(
            "3tt1",
            "Crystal Structure of LeuT in the outward-open conformation in complex with Fab"
        ),
        PdbEntryInfo(
            "3tt3",
            "Crystal Structure of LeuT in the inward-open conformation in complex with Fab"
        ),
        PdbEntryInfo("3twy", "RAT PKC C2 DOMAIN BOUND TO PB"),
        PdbEntryInfo(
            "3u5z",
            "Structure of T4 Bacteriophage clamp loader bound to the T4 clamp, primer-template DNA, and ATP analog"
        ),
        PdbEntryInfo("3ugm", "Structure of TAL effector PthXo1 bound to its DNA target"),
        PdbEntryInfo(
            "3um7",
            "Crystal structure of the human two pore domain K+ ion channel TRAAK (K2P4.1)"
        ),
        PdbEntryInfo("3unf", "Mouse 20S immunoproteasome in complex with PR-957"),
        PdbEntryInfo(
            "3uus",
            "Crystal structure of the dATP inhibited E. coli class Ia ribonucleotide reductase complex"
        ),
        PdbEntryInfo(
            "3ux4",
            "Crystal structure of the urea channel from the human gastric pathogen Helicobacter pylori"
        ),
        PdbEntryInfo("3v3b", "Structure of the Stapled p53 Peptide Bound to Mdm2"),
        PdbEntryInfo("3v6o", "Leptin Receptor-antibody complex"),
        PdbEntryInfo(
            "3v6t",
            "Crystal structure of the DNA-bound dHax3, a TAL effector, at 1.85 angstrom"
        ),
        PdbEntryInfo(
            "3vcd",
            "Computationally Designed Self-assembling Octahedral Cage protein, O333, Crystallized in space group R32"
        ),
        PdbEntryInfo(
            "3vdx",
            "Structure of a 16 nm protein cage designed by fusing symmetric oligomeric domains"
        ),
        PdbEntryInfo("3vie", "HIV-gp41 fusion inhibitor Sifuvirtide"),
        PdbEntryInfo("3vkh", "X-ray structure of a functional full-length dynein motor domain"),
        PdbEntryInfo("3vne", "Structure of the ebolavirus protein VP24 from Sudan"),
        PdbEntryInfo(
            "3vrf",
            "The crystal structure of hemoglobin from woolly mammoth in the carbonmonoxy forms"
        ),
        PdbEntryInfo(
            "3vzs",
            "Crystal structure of PhaB from Ralstonia eutropha in complex with Acetoacetyl-CoA and NADP"
        ),
        PdbEntryInfo(
            "3wwj",
            "Crystal structure of an engineered sitagliptin-producing transaminase, ATA-117-Rd11"
        ),
        PdbEntryInfo("3zdo", "Tetramerization domain of Measles virus phosphoprotein"),
        PdbEntryInfo(
            "3zmk",
            "Anopheles funestus glutathione-s-transferase epsilon 2 (GSTe2) protein structure from different alelles: A single amino acid change confers high level of DDT resistance and cross resistance to permethrin in a major malaria vector in Africa"
        ),
        PdbEntryInfo(
            "3zml",
            "Anopheles funestus glutathione-s-transferase epsilon 2 (GSTe2) protein structure from different alelles: A single amino acid change confers high level of DDT resistance and cross resistance to permethrin in a major malaria vector in Africa"
        ),
        PdbEntryInfo(
            "3zoj",
            "High-resolution structure of Pichia Pastoris aquaporin Aqy1 at 0.88 A"
        ),
        PdbEntryInfo(
            "3zpk",
            "Atomic-resolution structure of a quadruplet cross-beta amyloid fibril"
        ),
        PdbEntryInfo(
            "4ajx",
            "Ligand controlled assembly of hexamers, dihexamers, and linear multihexamer structures by an engineered acylated insulin"
        ),
        PdbEntryInfo(
            "4ald",
            "HUMAN MUSCLE FRUCTOSE 1,6-BISPHOSPHATE ALDOLASE COMPLEXED WITH FRUCTOSE 1,6-BISPHOSPHATE"
        ),
        PdbEntryInfo("4ape", "THE ACTIVE SITE OF ASPARTIC PROTEINASES"),
        PdbEntryInfo("4ar7", "X-ray structure of the cyan fluorescent protein mTurquoise"),
        PdbEntryInfo("4awb", "Crystal structure of active legumain in complex with AAN-CMK"),
        PdbEntryInfo(
            "4blm",
            "BETA-LACTAMASE OF BACILLUS LICHENIFORMIS 749(SLASH)C. REFINEMENT AT 2 ANGSTROMS RESOLUTION AND ANALYSIS OF HYDRATION"
        ),
        PdbEntryInfo(
            "4c6i",
            "Crystal structure of the dihydroorotase domain of human CAD bound to substrate at pH 7.0"
        ),
        PdbEntryInfo(
            "4cc8",
            "Pre-fusion structure of trimeric HIV-1 envelope glycoprotein determined by cryo-electron microscopy"
        ),
        PdbEntryInfo(
            "4cms",
            "X-RAY ANALYSES OF ASPARTIC PROTEINASES IV. STRUCTURE AND REFINEMENT AT 2.2 ANGSTROMS RESOLUTION OF BOVINE CHYMOSIN"
        ),
        PdbEntryInfo(
            "4cox",
            "CYCLOOXYGENASE-2 (PROSTAGLANDIN SYNTHASE-2) COMPLEXED WITH A NON-SELECTIVE INHIBITOR, INDOMETHACIN"
        ),
        PdbEntryInfo("4djh", "Structure of the human kappa opioid receptor in complex with JDTic"),
        PdbEntryInfo(
            "4dkl",
            "Crystal structure of the mu-opioid receptor bound to a morphinan antagonist"
        ),
        PdbEntryInfo("4dpv", "PARVOVIRUS/DNA COMPLEX"),
        PdbEntryInfo(
            "4ea3",
            "Structure of the N/OFQ Opioid Receptor in Complex with a Peptide Mimetic"
        ),
        PdbEntryInfo("4eeu", "Crystal structure of phiLOV2.1"),
        PdbEntryInfo("4egg", "Computationally Designed Self-assembling tetrahedron protein, T310"),
        PdbEntryInfo("4ej4", "Structure of the delta opioid receptor bound to naltrindole"),
        PdbEntryInfo("4ers", "A Molecular Basis for Negative Regulation of the Glucagon Receptor"),
        PdbEntryInfo(
            "4esv",
            "A New Twist on the Translocation Mechanism of Helicases from the Structure of DnaB with its Substrates"
        ),
        PdbEntryInfo("4eyl", "Crystal structure of NDM-1 bound to hydrolyzed meropenem"),
        PdbEntryInfo("4fgu", "Crystal structure of prolegumain"),
        PdbEntryInfo(
            "4fqi",
            "Crystal Structure of Fab CR9114 in Complex with a H5N1 influenza virus hemagglutinin"
        ),
        PdbEntryInfo("4fyw", "E. coli Aspartate Transcarbamoylase complexed with CTP"),
        PdbEntryInfo("4g1g", "Crystal structure of Newcastle disease virus matrix protein"),
        PdbEntryInfo("4gbc", "Crystal structure of aspart insulin at pH 6.5"),
        PdbEntryInfo(
            "4gcr",
            "STRUCTURE OF THE BOVINE EYE LENS PROTEIN GAMMA-B (GAMMA-II)-CRYSTALLIN AT 1.47 ANGSTROMS"
        ),
        PdbEntryInfo("4gcz", "Structure of a blue-light photoreceptor"),
        PdbEntryInfo("4gjt", "complex structure of nectin-4 bound to MV-H"),
        PdbEntryInfo("4gsl", "Crystal structure of an Atg7-Atg3 crosslinked complex"),
        PdbEntryInfo(
            "4hfe",
            "The GLIC pentameric Ligand-Gated Ion Channel F14'A ethanol-sensitive mutant complexed to ethanol"
        ),
        PdbEntryInfo(
            "4hhd",
            "2.75 Angstrom resolution crystal structure of the A. thaliana LOV2 domain with an extended N-terminal A' helix (cryo dark structure)"
        ),
        PdbEntryInfo(
            "4iao",
            "Crystal structure of Sir2 C543S mutant in complex with SID domain of Sir4"
        ),
        PdbEntryInfo(
            "4iar",
            "Crystal structure of the chimeric protein of 5-HT1B-BRIL in complex with ergotamine (PSI Community Target)"
        ),
        PdbEntryInfo(
            "4ib4",
            "Crystal structure of the chimeric protein of 5-HT2B-BRIL in complex with ergotamine"
        ),
        PdbEntryInfo(
            "4icd",
            "REGULATION OF ISOCITRATE DEHYDROGENASE BY PHOSPHORYLATION INVOLVES NO LONG-RANGE CONFORMATIONAL CHANGE IN THE FREE ENZYME"
        ),
        PdbEntryInfo(
            "4imv",
            "Ricin A-chain variant 1-33/44-198 with engineered disulfide bond, R48C/T77C/D75N"
        ),
        PdbEntryInfo(
            "4ins",
            "THE STRUCTURE OF 2ZN PIG INSULIN CRYSTALS AT 1.5 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo("4iyf", "Insulin glargine crystal structure 2"),
        PdbEntryInfo(
            "4j7u",
            "Crystal structure of human sepiapterin reductase in complex with sulfathiazole"
        ),
        PdbEntryInfo(
            "4jhw",
            "Crystal Structure of Respiratory Syncytial Virus Fusion Glycoprotein Stabilized in the Prefusion Conformation by Human Antibody D25"
        ),
        PdbEntryInfo("4jj0", "Crystal structure of MamP"),
        PdbEntryInfo("4kbp", "KIDNEY BEAN PURPLE ACID PHOSPHATASE"),
        PdbEntryInfo(
            "4kf5",
            "Crystal Structure of Split GFP complexed with engineered sfCherry with an insertion of GFP fragment"
        ),
        PdbEntryInfo("4kqw", "The structure of the Slackia exigua KARI in complex with NADP"),
        PdbEntryInfo("4kqx", "Mutant Slackia exigua KARI DDV in complex with NAD and an inhibitor"),
        PdbEntryInfo(
            "4kzd",
            "Crystal structure of an RNA aptamer in complex with fluorophore and Fab"
        ),
        PdbEntryInfo("4l6r", "Structure of the class B human glucagon G protein coupled receptor"),
        PdbEntryInfo("4ldb", "Crystal Structure of Ebola Virus VP40 Dimer"),
        PdbEntryInfo("4ldd", "Crystal Structure of Ebola virus VP40 Hexamer"),
        PdbEntryInfo(
            "4lp5",
            "Crystal structure of the full-length human RAGE extracellular domain (VC1C2 fragment)"
        ),
        PdbEntryInfo(
            "4lsx",
            "Plant steroid receptor ectodomain bound to brassinolide and SERK1 co-receptor ectodomain"
        ),
        PdbEntryInfo(
            "4m48",
            "X-ray structure of dopamine transporter elucidates antidepressant mechanism"
        ),
        PdbEntryInfo(
            "4m4w",
            "Mechanistic implications for the bacterial primosome assembly of the structure of a helicase-helicase loader complex"
        ),
        PdbEntryInfo(
            "4mmv",
            "Crystal Structure of Prefusion-stabilized RSV F Variant DS-Cav1-TriC at pH 9.5"
        ),
        PdbEntryInfo(
            "4mn8",
            "Crystal structure of flg22 in complex with the FLS2 and BAK1 ectodomains"
        ),
        PdbEntryInfo("4mne", "Crystal structure of the BRAF:MEK1 complex"),
        PdbEntryInfo("4n6o", "Crystal structure of reduced legumain in complex with cystatin E/M"),
        PdbEntryInfo(
            "4nco",
            "Crystal Structure of the BG505 SOSIP gp140 HIV-1 Env trimer in Complex with the Broadly Neutralizing Fab PGT122"
        ),
        PdbEntryInfo(
            "4o9c",
            "Crystal structure of Beta-ketothiolase (PhaA) from Ralstonia eutropha H16"
        ),
        PdbEntryInfo(
            "4oaa",
            "Crystal structure of E. coli lactose permease G46W,G262W bound to sugar"
        ),
        PdbEntryInfo(
            "4oo8",
            "Crystal structure of Streptococcus pyogenes Cas9 in complex with guide RNA and target DNA"
        ),
        PdbEntryInfo(
            "4or2",
            "Human class C G protein-coupled metabotropic glutamate receptor 1 in complex with a negative allosteric modulator"
        ),
        PdbEntryInfo(
            "4ow0",
            "X-Ray Structural and Biological Evaluation of a Series of Potent and Highly Selective Inhibitors of Human Coronavirus Papain-Like Proteases"
        ),
        PdbEntryInfo("4p1w", "Crystal structure of Atg13(17BR)-Atg17-Atg29-Atg31 complex"),
        PdbEntryInfo("4p6i", "Crystal structure of the Cas1-Cas2 complex from Escherichia coli"),
        PdbEntryInfo("4pe5", "Crystal Structure of GluN1a/GluN2B NMDA Receptor Ion Channel"),
        PdbEntryInfo("4pfk", "PHOSPHOFRUCTOKINASE. STRUCTURE AND CONTROL"),
        PdbEntryInfo(
            "4prq",
            "CRYSTAL STRUCTURE OF HEN EGG-WHITE LYSOZYME IN COMPLEX WITH SCLX4 AT 1.72 A RESOLUTION"
        ),
        PdbEntryInfo(
            "4pti",
            "THE GEOMETRY OF THE REACTIVE SITE AND OF THE PEPTIDE GROUPS IN TRYPSIN, TRYPSINOGEN AND ITS COMPLEXES WITH INHIBITORS"
        ),
        PdbEntryInfo("4pyp", "Crystal structure of the human glucose transporter GLUT1"),
        PdbEntryInfo(
            "4q21",
            "MOLECULAR SWITCH FOR SIGNAL TRANSDUCTION: STRUCTURAL DIFFERENCES BETWEEN ACTIVE AND INACTIVE FORMS OF PROTOONCOGENIC RAS PROTEINS"
        ),
        PdbEntryInfo(
            "4qb0",
            "The crystal structure of the C-terminal domain of Ebola (Zaire) nucleoprotein"
        ),
        PdbEntryInfo("4qqw", "Crystal structure of T. fusca Cas3"),
        PdbEntryInfo(
            "4qyz",
            "Crystal structure of a CRISPR RNA-guided surveillance complex, Cascade, bound to a ssDNA target"
        ),
        PdbEntryInfo("4r8f", "Crystal structure of yeast aminopeptidase 1 (Ape1)"),
        PdbEntryInfo(
            "4rhv",
            "THE USE OF MOLECULAR-REPLACEMENT PHASES FOR THE REFINEMENT OF THE HUMAN RHINOVIRUS 14 STRUCTURE"
        ),
        PdbEntryInfo(
            "4rxn",
            "CRYSTALLOGRAPHIC REFINEMENT OF RUBREDOXIN AT 1.2 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo("4tna", "FURTHER REFINEMENT OF THE STRUCTURE OF YEAST T-RNA-PHE"),
        PdbEntryInfo(
            "4tpw",
            "The co-complex structure of the translation initiation factor eIF4E with the inhibitor 4EGI-1 reveals an allosteric mechanism for dissociating eIF4G"
        ),
        PdbEntryInfo(
            "4ts2",
            "Crystal structure of the Spinach RNA aptamer in complex with DFHBI, magnesium ions"
        ),
        PdbEntryInfo(
            "4u5c",
            "Crystal structure of GluA2, con-ikot-ikot snail toxin, partial agonist FW and postitive modulator (R,R)-2b complex"
        ),
        PdbEntryInfo("4ued", "Complex of human eIF4E with the 4E binding protein 4E-BP1"),
        PdbEntryInfo("4uft", "Structure of the helical Measles virus nucleocapsid"),
        PdbEntryInfo("4un3", "Crystal structure of Cas9 bound to PAM-containing DNA target"),
        PdbEntryInfo(
            "4ur0",
            "Crystal structure of the PCE reductive dehalogenase from S. multivorans in complex with trichloroethene"
        ),
        PdbEntryInfo(
            "4uww",
            "Crystallographic Structure of the Intramineral Protein Struthicalcin from Struthio camelus Eggshell"
        ),
        PdbEntryInfo(
            "4xia",
            "STRUCTURES OF D-XYLOSE ISOMERASE FROM ARTHROBACTER STRAIN B3728 CONTAINING THE INHIBITORS XYLITOL AND D-SORBITOL AT 2.5 ANGSTROMS AND 2.3 ANGSTROMS RESOLUTION, RESPECTIVELY"
        ),
        PdbEntryInfo("4xmm", "Structure of the yeast coat nucleoporin complex, space group C2"),
        PdbEntryInfo(
            "4xr8",
            "Crystal structure of the HPV16 E6/E6AP/p53 ternary complex at 2.25 A resolution"
        ),
        PdbEntryInfo("4xv1", "B-Raf Kinase V600E oncogenic mutant in complex with PLX7904"),
        PdbEntryInfo("4xv2", "B-Raf Kinase V600E oncogenic mutant in complex with Dabrafenib"),
        PdbEntryInfo(
            "4yb9",
            "Crystal structure of the Bovine Fructose transporter GLUT5 in an open inward-facing conformation"
        ),
        PdbEntryInfo("4ybq", "Rat GLUT5 with Fv in the outward-open form"),
        PdbEntryInfo("4yoi", "Structure of HKU4 3CLpro bound to non-covalent inhibitor 1A"),
        PdbEntryInfo("4z62", "The plant peptide hormone free receptor"),
        PdbEntryInfo("4z63", "The plant peptide hormone receptor in arabidopsis"),
        PdbEntryInfo("4z64", "the plant peptide hormone receptor complex in arabidopsis"),
        PdbEntryInfo("4zhd", "Siderocalin-mediated recognition and cellular uptake of actinides"),
        PdbEntryInfo(
            "4zpr",
            "Crystal Structure of the Heterodimeric HIF-1a:ARNT Complex with HRE DNA"
        ),
        PdbEntryInfo(
            "4zqk",
            "Structure of the complex of human programmed death-1 (PD-1) and its ligand PD-L1."
        ),
        PdbEntryInfo(
            "4zwc",
            "Crystal structure of maltose-bound human GLUT3 in the outward-open conformation at 2.6 angstrom"
        ),
        PdbEntryInfo(
            "5a22",
            "Structure of the L protein of vesicular stomatitis virus from electron cryomicroscopy"
        ),
        PdbEntryInfo("5ara", "Bovine mitochondrial ATP synthase state 1a"),
        PdbEntryInfo(
            "5at1",
            "STRUCTURAL CONSEQUENCES OF EFFECTOR BINDING TO THE T STATE OF ASPARTATE CARBAMOYLTRANSFERASE. CRYSTAL STRUCTURES OF THE UNLIGATED AND ATP-, AND CTP-COMPLEXED ENZYMES AT 2.6-ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "5bjp",
            "Crystal structure of the Corn RNA aptamer in complex with DFHO, iridium hexammine soak"
        ),
        PdbEntryInfo(
            "5btr",
            "Crystal structure of SIRT1 in complex with resveratrol and an AMC-containing peptide"
        ),
        PdbEntryInfo(
            "5cfs",
            "Crystal Structure of ANT(2\")-Ia in complex with AMPCPP and tobramycin"
        ),
        PdbEntryInfo(
            "5cfu",
            "Crystal Structure of ANT(2\")-Ia in complex with adenylyl-2\"-tobramycin"
        ),
        PdbEntryInfo(
            "5chb",
            "Crystal structure of nvPizza2-S16H58 coordinating a CdCl2 nanocrystal"
        ),
        PdbEntryInfo(
            "5dis",
            "Crystal structure of a CRM1-RanGTP-SPN1 export complex bound to a 113 amino acid FG-repeat containing fragment of Nup214"
        ),
        PdbEntryInfo("5dk3", "Crystal Structure of Pembrolizumab, a full length IgG4 antibody"),
        PdbEntryInfo(
            "5dou",
            "Crystal Structure of Human Carbamoyl phosphate synthetase I (CPS1), ligand-bound form"
        ),
        PdbEntryInfo("5e33", "Structure of human DPP3 in complex with met-enkephalin"),
        PdbEntryInfo("5e4v", "Crystal structure of measles N0-P complex"),
        PdbEntryInfo(
            "5e54",
            "Two apo structures of the adenine riboswitch aptamer domain determined using an X-ray free electron laser"
        ),
        PdbEntryInfo("5eqi", "Human GLUT1 in complex with Cytochalasin B"),
        PdbEntryInfo("5et3", "Crystal Structure of De novo Designed Fullerene organizing peptide"),
        PdbEntryInfo("5g1n", "Aspartate transcarbamoylase domain of human CAD bound to PALA"),
        PdbEntryInfo("5ire", "The cryo-EM structure of Zika Virus"),
        PdbEntryInfo("5j7v", "Faustovirus major capsid protein"),
        PdbEntryInfo(
            "5j89",
            "Structure of human Programmed cell death 1 ligand 1 (PD-L1) with low molecular mass inhibitor"
        ),
        PdbEntryInfo("5jh9", "Crystal structure of prApe1"),
        PdbEntryInfo("5jm0", "Structure of the S. cerevisiae alpha-mannosidase 1"),
        PdbEntryInfo("5jxe", "Human PD-1 ectodomain complexed with Pembrolizumab Fab"),
        PdbEntryInfo("5ks9", "Bel502-DQ8-glia-alpha1 complex"),
        PdbEntryInfo("5kuf", "GluK2EM with 2S,4R-4-methylglutamate"),
        PdbEntryInfo("5l2s", "The X-ray co-crystal structure of human CDK6 and Abemaciclib."),
        PdbEntryInfo("5l2t", "The X-ray co-crystal structure of human CDK6 and Ribociclib."),
        PdbEntryInfo(
            "5lf5",
            "Myelin-associated glycoprotein (MAG) deglycosylated full extracellular domain with co-purified ligand"
        ),
        PdbEntryInfo(
            "5lpb",
            "Crystal structure of the BRI1 kinase domain (865-1160) in complex with ADP from Arabidopsis thaliana"
        ),
        PdbEntryInfo(
            "5m2g",
            "PCE reductive dehalogenase from S. multivorans in complex with 2,4,6-tribromophenol"
        ),
        PdbEntryInfo(
            "5m8u",
            "PCE reductive dehalogenase from S. multivorans in complex with 4-bromophenol"
        ),
        PdbEntryInfo(
            "5m92",
            "PCE reductive dehalogenase from S. multivorans in complex with 2,4-dibromophenol"
        ),
        PdbEntryInfo(
            "5mdh",
            "CRYSTAL STRUCTURE OF TERNARY COMPLEX OF PORCINE CYTOPLASMIC MALATE DEHYDROGENASE ALPHA-KETOMALONATE AND TNAD AT 2.4 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo("5mf6", "Human Sirt6 in complex with activator UBCS039"),
        PdbEntryInfo(
            "5nij",
            "Crystal structure of arabidopsis thaliana legumain isoform gamma in two-chain activation state"
        ),
        PdbEntryInfo("5oeh", "Molecular tweezers modulate 14-3-3 protein-protein interactions."),
        PdbEntryInfo("5ox6", "HIF prolyl hydroxylase 2 (PHD2/ EGLN1) in complex with Vadadustat"),
        PdbEntryInfo(
            "5p21",
            "REFINED CRYSTAL STRUCTURE OF THE TRIPHOSPHATE CONFORMATION OF H-RAS P21 AT 1.35 ANGSTROMS RESOLUTION: IMPLICATIONS FOR THE MECHANISM OF GTP HYDROLYSIS"
        ),
        PdbEntryInfo(
            "5pep",
            "X-RAY ANALYSES OF ASPARTIC PROTEASES. II. THREE-DIMENSIONAL STRUCTURE OF THE HEXAGONAL CRYSTAL FORM OF PORCINE PEPSIN AT 2.3 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo("5rsa", "COMPARISON OF TWO INDEPENDENTLY REFINED MODELS OF RIBONUCLEASE-A"),
        PdbEntryInfo(
            "5swd",
            "Structure of the adenine riboswitch aptamer domain in an intermediate-bound state"
        ),
        PdbEntryInfo(
            "5swe",
            "Ligand-bound structure of adenine riboswitch aptamer domain converted in crystal from its ligand-free state using ligand mixing serial femtosecond crystallography"
        ),
        PdbEntryInfo("5t46", "Crystal structure of the human eIF4E-eIF4G complex"),
        PdbEntryInfo(
            "5t6o",
            "Structure of the catalytic domain of the class I polyhydroxybutyrate synthase from Cupriavidus necator"
        ),
        PdbEntryInfo(
            "5tus",
            "Potent competitive inhibition of human ribonucleotide reductase by a novel non-nucleoside small molecule"
        ),
        PdbEntryInfo("5tzo", "Computationally Designed Fentanyl Binder - Fen49*-Complex"),
        PdbEntryInfo("5ucw", "Cytochrome P411 P-4 A82L A78V F263L amination catalyst"),
        PdbEntryInfo(
            "5vb8",
            "Crystal structure of the NavAb voltage-gated sodium channel in an open state"
        ),
        PdbEntryInfo(
            "5w1o",
            "Crystal Structure of HPV16 L1 Pentamer Bound to Heparin Oligosaccharides"
        ),
        PdbEntryInfo(
            "5weo",
            "Activated GluA2 complex bound to glutamate, cyclothiazide, and STZ in digitonin"
        ),
        PdbEntryInfo(
            "5yxw",
            "Crystal structure of the prefusion form of measles virus fusion protein"
        ),
        PdbEntryInfo("5z10", "Structure of the mechanosensitive Piezo1 channel"),
        PdbEntryInfo(
            "6a95",
            "Complex of voltage-gated sodium channel NavPaS from American cockroach Periplaneta americana bound with tetrodotoxin and Dc1a"
        ),
        PdbEntryInfo(
            "6adh",
            "STRUCTURE OF TRICLINIC TERNARY COMPLEX OF HORSE LIVER ALCOHOL DEHYDROGENASE AT 2.9 ANGSTROMS RESOLUTION"
        ),
        PdbEntryInfo(
            "6am8",
            "Engineered tryptophan synthase b-subunit from Pyrococcus furiosus, PfTrpB2B9 with Trp bound as E(Aex2)"
        ),
        PdbEntryInfo("6b3r", "Structure of the mechanosensitive channel Piezo1"),
        PdbEntryInfo("6bpz", "Structure of the mechanically activated ion channel Piezo1"),
        PdbEntryInfo(
            "6bt3",
            "High-Resolution Structure Analysis of Antibody V5 Conformational Epitope on Human Papillomavirus 16"
        ),
        PdbEntryInfo(
            "6c63",
            "Crystal Structure of the Mango-II Fluorescent Aptamer Bound to TO1-Biotin"
        ),
        PdbEntryInfo(
            "6c6k",
            "Structural basis for preferential recognition of cap 0 RNA by a human IFIT1-IFIT3 protein complex"
        ),
        PdbEntryInfo(
            "6cfz",
            "Structure of the DASH/Dam1 complex shows its role at the yeast kinetochore-microtubule interface"
        ),
        PdbEntryInfo("6clz", "MT1-MMP HPX domain with Blade 4 Loop Bound to Nanodiscs"),
        PdbEntryInfo(
            "6d6v",
            "CryoEM structure of Tetrahymena telomerase with telomeric DNA at 4.8 Angstrom resolution"
        ),
        PdbEntryInfo(
            "6db8",
            "Structural basis for promiscuous binding and activation of fluorogenic dyes by DIR2s RNA aptamer"
        ),
        PdbEntryInfo(
            "6gpb",
            "REFINED CRYSTAL STRUCTURE OF THE PHOSPHORYLASE-HEPTULOSE 2-PHOSPHATE-OLIGOSACCHARIDE-AMP COMPLEX"
        ),
        PdbEntryInfo(
            "6j4y",
            "RNA polymerase II elongation complex bound with Elf1 and Spt4/5, stalled at SHL(-1) of the nucleosome (+1B)"
        ),
        PdbEntryInfo(
            "6j8j",
            "Structure of human voltage-gated sodium channel Nav1.7 in complex with auxiliary beta subunits, ProTx-II and tetrodotoxin (Y1755 down)"
        ),
        PdbEntryInfo("6ldh", "REFINED CRYSTAL STRUCTURE OF DOGFISH M4 APO-LACTATE DEHYDROGENASE"),
        PdbEntryInfo(
            "6lu7",
            "The crystal structure of COVID-19 main protease in complex with an inhibitor N3"
        ),
        PdbEntryInfo("6m17", "The 2019-nCoV RBD/ACE2-B0AT1 complex"),
        PdbEntryInfo(
            "6mam",
            "Cleaved Ebola GP in complex with a broadly neutralizing human antibody, ADI-15946"
        ),
        PdbEntryInfo("6n7p", "S. cerevisiae spliceosomal E complex (UBC4)"),
        //PdbEntryInfo("6o85", "Electron cryo-microscopy of the eukaryotic translation initiation factor 2B bound to eukaryotic translation initiation factor 2 from Homo sapiens"),
        PdbEntryInfo(
            "6p6w",
            "Cryo-EM structure of voltage-gated sodium channel NavAb N49K/L109A/M116V/G94C/Q150C disulfide crosslinked mutant in the resting state"
        ),
        PdbEntryInfo("6pfk", "PHOSPHOFRUCTOKINASE, INHIBITED T-STATE"),
        PdbEntryInfo(
            "6tna",
            "CRYSTAL STRUCTURE OF YEAST PHENYLALANINE T-RNA. I.CRYSTALLOGRAPHIC REFINEMENT"
        ),
        PdbEntryInfo(
            "6vsb",
            "Prefusion 2019-nCoV spike glycoprotein with a single receptor-binding domain up"
        ),
        PdbEntryInfo("6vxx", "Structure of the SARS-CoV-2 spike glycoprotein (closed state)"),
        PdbEntryInfo(
            "6w41",
            "Crystal structure of SARS-CoV-2 receptor binding domain in complex with human antibody CR3022"
        ),
        PdbEntryInfo(
            "6w4x",
            "Holocomplex of E. coli class Ia ribonucleotide reductase with GDP and TTP"
        ),
        PdbEntryInfo("6yyt", "Structure of replicating SARS-CoV-2 polymerase"),
        PdbEntryInfo(
            "7acn",
            "CRYSTAL STRUCTURES OF ACONITASE WITH ISOCITRATE AND NITROISOCITRATE BOUND"
        ),
        PdbEntryInfo(
            "7bv2",
            "The nsp12-nsp7-nsp8 complex bound to the template-primer RNA and triphosphate form of Remdesivir(RTP)"
        ),
        PdbEntryInfo(
            "7bzf",
            "COVID-19 RNA-dependent RNA polymerase post-translocated catalytic complex"
        ),
        PdbEntryInfo(
            "7c2k",
            "COVID-19 RNA-dependent RNA polymerase pre-translocated catalytic complex"
        ),
        PdbEntryInfo(
            "7dfr",
            "CRYSTAL STRUCTURES OF ESCHERICHIA COLI DIHYDROFOLATE REDUCTASE. THE NADP+ HOLOENZYME AND THE FOLATE(DOT)NADP+ TERNARY COMPLEX. SUBSTRATE BINDING AND A MODEL FOR THE TRANSITION STATE"
        ),
        PdbEntryInfo(
            "7hvp",
            "X-RAY CRYSTALLOGRAPHIC STRUCTURE OF A COMPLEX BETWEEN A SYNTHETIC PROTEASE OF HUMAN IMMUNODEFICIENCY VIRUS 1 AND A SUBSTRATE-BASED HYDROXYETHYLAMINE INHIBITOR"
        ),
        PdbEntryInfo("8cat", "The NADPH binding site on beef liver catalase"),
        PdbEntryInfo(
            "8gpb",
            "STRUCTURAL MECHANISM FOR GLYCOGEN PHOSPHORYLASE CONTROL BY PHOSPHORYLATION AND AMP"
        ),
        PdbEntryInfo("8icd", "REGULATION OF AN ENZYME BY PHOSPHORYLATION AT THE ACTIVE SITE"),
        PdbEntryInfo(
            "8ruc",
            "ACTIVATED SPINACH RUBISCO COMPLEXED WITH 2-CARBOXYARABINITOL BISPHOSPHATE"
        ),
        PdbEntryInfo(
            "9icd",
            "CATALYTIC MECHANISM OF NADP+-DEPENDENT ISOCITRATE DEHYDROGENASE: IMPLICATIONS FROM THE STRUCTURES OF MAGNESIUM-ISOCITRATE AND NADP+ COMPLEXES"
        ),
        PdbEntryInfo("9pap", "STRUCTURE OF PAPAIN REFINED AT 1.65 ANGSTROMS RESOLUTION"),
        PdbEntryInfo(
            "9rub",
            "CRYSTAL STRUCTURE OF ACTIVATED RIBULOSE-1,5-BISPHOSPHATE CARBOXYLASE COMPLEXED WITH ITS SUBSTRATE, RIBULOSE-1,5-BISPHOSPHATE"
        ),

        // -----------------------------------------------
        // new entries - were previously too big to render
        // -----------------------------------------------


        PdbEntryInfo(
            "2tbv",
            "STRUCTURE OF TOMATO BUSHY STUNT VIRUS. V. COAT PROTEIN SEQUENCE DETERMINATION AND ITS STRUCTURAL IMPLICATIONS"
        ),
// 1tzo was too big
        PdbEntryInfo(
            "1tzo",
            "Crystal Structure of the Anthrax Toxin Protective Antigen Heptameric Prepore"
        ),
// 1iw7
        PdbEntryInfo(
            "1iw7",
            "Crystal structure of the RNA polymerase holoenzyme from Thermus thermophilus at 2.6A resolution"
        ),
// MotmToPdbMap.put(86, "2c37")
        PdbEntryInfo("1w63", "AP1 clathrin adaptor core"),

//  MotmToPdbMap.put(121, "1nji") // too big 98566 atoms

        PdbEntryInfo(
            "1hnw",
            "STRUCTURE OF THE THERMUS THERMOPHILUS 30S RIBOSOMAL SUBUNIT IN COMPLEX WITH TETRACYCLINE"
        ),
        PdbEntryInfo("1nji", "Structure of chloramphenicol bound to the 50S ribosomal subunit"),

        PdbEntryInfo(
            "5xnl",
            "Structure of stacked C2S2M2-type PSII-LHCII supercomplex from Pisum sativum"
        ),
        PdbEntryInfo(
            "6kac",
            "Cryo-EM structure of the C2S2-type PSII-LHCII supercomplex from Chlamydomonas reihardtii"
        ),
        PdbEntryInfo(
            "3jb9",
            "Cryo-EM structure of the yeast spliceosome at 3.6 angstrom resolution"
        ),
        PdbEntryInfo(
            "1fjg",
            "STRUCTURE OF THE THERMUS THERMOPHILUS 30S RIBOSOMAL SUBUNIT IN COMPLEX WITH THE ANTIBIOTICS STREPTOMYCIN, SPECTINOMYCIN, AND PAROMOMYCIN"
        ),
        PdbEntryInfo(
            "4ox9",
            "Crystal structure of the aminoglycoside resistance methyltransferase NpmA bound to the 30S ribosomal subunit"
        ),
        PdbEntryInfo(
            "5y9f",
            "Crystal structure of HPV59 pentamer in complex with the Fab fragment of antibody 28F10"
        ),
        PdbEntryInfo(
            "5ijn",
            "Composite structure of the inner ring of the human nuclear pore complex (32 copies of Nup205)"
        ),
        PdbEntryInfo("5a9q", "Human nuclear pore complex"),


        PdbEntryInfo(
            "4tnv",
            "C. elegans glutamate-gated chloride channel (GluCl) in complex with Fab in a non-conducting conformation"
        ),
        PdbEntryInfo("4u7u", "Crystal structure of RNA-guided immune Cascade complex from E.coli"),
        PdbEntryInfo(
            "4cr2",
            "Deep classification of a large cryo-EM dataset defines the conformational landscape of the 26S proteasome"
        ),
        PdbEntryInfo("1pma", "PROTEASOME FROM THERMOPLASMA ACIDOPHILUM"),

        //PdbEntryInfo("6ezo", "Eukaryotic initiation factor EIF2B in complex with ISRIB"),  // special interest
        PdbEntryInfo(
            "1ibk",
            "STRUCTURE OF THE THERMUS THERMOPHILUS 30S RIBOSOMAL SUBUNIT IN COMPLEX WITH THE ANTIBIOTIC PAROMOMYCIN"
        ),
        PdbEntryInfo(
            "1ibl",
            "STRUCTURE OF THE THERMUS THERMOPHILUS 30S RIBOSOMAL SUBUNIT IN COMPLEX WITH A MESSENGER RNA FRAGMENT AND COGNATE TRANSFER RNA ANTICODON STEM-LOOP BOUND AT THE A SITE AND WITH THE ANTIBIOTIC PAROMOMYCIN"
        ),
        PdbEntryInfo(
            "1ibm",
            "STRUCTURE OF THE THERMUS THERMOPHILUS 30S RIBOSOMAL SUBUNIT IN COMPLEX WITH A MESSENGER RNA FRAGMENT AND COGNATE TRANSFER RNA ANTICODON STEM-LOOP BOUND AT THE A SITE"
        ),
        PdbEntryInfo("1j5e", "Structure of the Thermus thermophilus 30S Ribosomal Subunit"),


        PdbEntryInfo(
            "6cgv",
            "Revised crystal structure of human adenovirus"
        ),  //Entry: 6CGV supersedes: 4CWU
        PdbEntryInfo("3aic", "Crystal Structure of Glucansucrase from Streptococcus mutans"),
        PdbEntryInfo("3aie", "Crystal Structure of glucansucrase from Streptococcus mutans"),
        PdbEntryInfo(
            "2fug",
            "Crystal structure of the hydrophilic domain of respiratory complex I from Thermus thermophilus"
        ),

        PdbEntryInfo(
            "1yqv",
            "The crystal structure of the antibody Fab HyHEL5 complex with lysozyme at 1.7A resolution"
        ),  // was 3HFL
        PdbEntryInfo("3cf1", "Structure of P97/vcp in complex with ADP/ADP.alfx"),
        PdbEntryInfo("5owu", "Kap95:Nup1 complex"),  // Entry: 5OWU supersedes: 2BPT
        PdbEntryInfo("2c37", "RNASE PH CORE OF THE ARCHAEAL EXOSOME IN COMPLEX WITH U8 RNA"),
        PdbEntryInfo("2vgl", "AP2 CLATHRIN ADAPTOR CORE"),
        PdbEntryInfo("1xi4", "Clathrin D6 Coat"),
        PdbEntryInfo(
            "3snp",
            "Crystal structure analysis of iron regulatory protein 1 in complex with ferritin H IRE RNA"
        ),
        PdbEntryInfo(
            "4ac9",
            "CRYSTAL STRUCTURE OF TRANSLATION ELONGATION FACTOR SELB FROM METHANOCOCCUS MARIPALUDIS IN COMPLEX WITH GDP"
        ),
        PdbEntryInfo("5ksd", "Crystal Structure of a Plasma Membrane Proton Pump"),
        PdbEntryInfo(
            "4fe5",
            "Crystal structure of the xpt-pbuX guanine riboswitch aptamer domain in complex with hypoxanthine"
        ),
        PdbEntryInfo("3fcs", "Structure of complete ectodomain of integrin aIIBb3"),
        PdbEntryInfo("5ugy", "Influenza hemagglutinin in complex with a neutralizing antibody"),
// CIF only           PdbEntryInfo("4tvx", "Crystal structure of the E. coli CRISPR RNA-guided surveillance complex, Cascade"),
        PdbEntryInfo(
            "4zxb",
            "Structure of the human insulin receptor ectodomain, IRDeltabeta construct, in complex with four Fab molecules"
        ),
        PdbEntryInfo(
            "5kqv",
            "Insulin receptor ectodomain construct comprising domains L1,CR,L2, FnIII-1 and alphaCT peptide in complex with bovine insulin and FAB 83-14 (REVISED STRUCTURE)"
        ),
        PdbEntryInfo(
            "5zge",
            "Crystal structure of NDM-1 at pH5.5 (Bis-Tris) in complex with hydrolyzed ampicillin"
        ),
        PdbEntryInfo(
            "3rif",
            "C. elegans glutamate-gated chloride channel (GluCl) in complex with Fab, ivermectin and glutamate."
        ),
        PdbEntryInfo(
            "5a1a",
            "2.2 A resolution cryo-EM structure of beta-galactosidase in complex with a cell-permeant inhibitor"
        ),
        PdbEntryInfo("2btv", "ATOMIC MODEL FOR BLUETONGUE VIRUS (BTV) CORE"),
        PdbEntryInfo("5gar", "Thermus thermophilus V/A-ATPase, conformation 1"),
        PdbEntryInfo(
            "5vox",
            "Yeast V-ATPase in complex with Legionella pneumophila effector SidK (rotational state 1)"
        ),
        PdbEntryInfo(
            "5voy",
            "Yeast V-ATPase in complex with Legionella pneumophila effector SidK (rotational state 2)"
        ),
        PdbEntryInfo(
            "5voz",
            "Yeast V-ATPase in complex with Legionella pneumophila effector SidK (rotational state 3)"
        ),
        PdbEntryInfo(
            "5vkq",
            "Structure of a mechanotransduction ion channel Drosophila NOMPC in nanodisc)"
        ),
        PdbEntryInfo("4xxb", "Crystal structure of human MDM2-RPL11"),
        PdbEntryInfo("5mnj", "Structure of MDM2-MDMX-UbcH5B-ubiquitin complex"),
        PdbEntryInfo(
            "5zji",
            "Structure of photosystem I supercomplex with light-harvesting complexes I and II"
        ),
        PdbEntryInfo(
            "6crz",
            "SARS Spike Glycoprotein, Trypsin-cleaved, Stabilized variant, C3 symmetry"
        ),
        PdbEntryInfo("6xqb", "SARS-CoV-2 RdRp/RNA complex, Entry: 6XQB supersedes: 6X2G"),
        PdbEntryInfo(
            "5is0",
            "Structure of TRPV1 in complex with capsazepine, determined in lipid nanodisc"
        ),
        PdbEntryInfo("6dmw", "Calmodulin-bound full-length rbTRPV5"),
        PdbEntryInfo("5irz", "Structure of TRPV1 determined in lipid nanodisc"),
        PdbEntryInfo(
            "5irx",
            "Structure of TRPV1 in complex with DkTx and RTX, determined in lipid nanodisc"
        ),
        PdbEntryInfo(
            "6r3q",
            "The structure of a membrane adenylyl cyclase bound to an activated stimulatory G protein"
        ),
        PdbEntryInfo(
            "4clk",
            "Crystal structure of human soluble Adenylyl Cyclase in complex with alpha,beta-methyleneadenosine-5'-triphosphate"
        ),
        PdbEntryInfo(
            "5u6p",
            "Structure of the human HCN1 hyperpolarization-activated cyclic nucleotide-gated ion channel in complex with cAMP"
        ),
        PdbEntryInfo(
            "1cjk",
            "COMPLEX OF GS-ALPHA WITH THE CATALYTIC DOMAINS OF MAMMALIAN ADENYLYL CYCLASE: COMPLEX WITH ADENOSINE 5'-(ALPHA THIO)-TRIPHOSPHATE (RP), MG, AND MN"
        ),
        PdbEntryInfo("1cu1", "CRYSTAL STRUCTURE OF AN ENZYME COMPLEX FROM HEPATITIS C VIRUS"),
        PdbEntryInfo(
            "4wtg",
            "CRYSTAL STRUCTURE OF HCV NS5B GENOTYPE 2A JFH-1 ISOLATE WITH S15G E86Q E87Q C223H V321I MUTATIONS AND DELTA8 BETA HAIRPIN LOOP DELETION IN COMPLEX WITH SOFOSBUVIR DIPHOSPHATE GS-607596, MN2+ AND SYMMETRICAL PRIMER TEMPLATE 5'-CAAAAUUU"
        ),
        PdbEntryInfo("1zh1", "Structure of the zinc-binding domain of HCV NS5A"),
        PdbEntryInfo(
            "1r7g",
            "NMR structure of the membrane anchor domain (1-31) of the nonstructural protein 5A (NS5A) of hepatitis C virus (Minimized average structure, Sample in 100mM DPC)"
        ),
        PdbEntryInfo(
            "2oc8",
            "Structure of Hepatitis C Viral NS3 protease domain complexed with NS4A peptide and ketoamide SCH503034"
        ),
        PdbEntryInfo("3sv6", "Crystal structure of NS3/4A protease in complex with Telaprevir"),
        PdbEntryInfo(
            "3sue",
            "Crystal structure of NS3/4A protease variant R155K in complex with MK-5172"
        ),
        PdbEntryInfo(
            "6p6l",
            "HCV NS3/4A protease domain of genotype 1a in complex with glecaprevir"
        ),
        PdbEntryInfo(
            "6nzt",
            "Crystal structure of HCV NS3/4A protease in complex with voxilaprevir"
        ),
        // Jan 2021
        PdbEntryInfo("5my1", "Architecture of a transcribing-translating expressome"),


        PdbEntryInfo ("1b86", "HUMAN DEOXYHAEMOGLOBIN-2,3-DIPHOSPHOGLYCERATE COMPLEX"),
            PdbEntryInfo("1ckt", "CRYSTAL STRUCTURE OF HMG1 DOMAIN A BOUND TO A CISPLATIN-MODIFIED DNA DUPLEX"),
            PdbEntryInfo("1fdh", "STRUCTURE OF HUMAN FOETAL DEOXYHAEMOGLOBIN"),
            PdbEntryInfo("1glu", "CRYSTALLOGRAPHIC ANALYSIS OF THE INTERACTION OF THE GLUCOCORTICOID RECEPTOR WITH DNA"),
            PdbEntryInfo("1m2z", "Crystal structure of a dimer complex of the human glucocorticoid receptor ligand-binding domain bound to dexamethasone and a TIF2 coactivator motif"),
            PdbEntryInfo("2h4z", "Human bisphosphoglycerate mutase complexed with 2,3-bisphosphoglycerate"),
            PdbEntryInfo("2icy", "Crystal Structure of a Putative UDP-glucose Pyrophosphorylase from Arabidopsis Thaliana with Bound UDP-glucose"),
            PdbEntryInfo("2r7z", "Cisplatin lesion containing RNA polymerase II elongation complex"),
            PdbEntryInfo("2r8k", "Structure of the Eukaryotic DNA Polymerase eta in complex with 1,2-d(GpG)-cisplatin containing DNA"),
            PdbEntryInfo("3dnb", "HELIX GEOMETRY, HYDRATION, AND G.A MISMATCH IN A B-DNA DECAMER"),
            PdbEntryInfo("3lpv", "X-ray crystal structure of duplex DNA containing a cisplatin 1,2-d(GpG) intrastrand cross-link"),
            PdbEntryInfo("3s27", "The crystal structure of sucrose synthase-1 from Arabidopsis thaliana and its functional implications."),
            PdbEntryInfo("4hg6", "Structure of a cellulose synthase - cellulose translocation intermediate"),
            PdbEntryInfo("4hhb", "THE CRYSTAL STRUCTURE OF HUMAN DEOXYHAEMOGLOBIN AT 1.74 ANGSTROMS RESOLUTION"),
            PdbEntryInfo("4k2c", "HSA Ligand Free"),
            PdbEntryInfo("4p6x", "Crystal Structure of cortisol-bound glucocorticoid receptor ligand binding domain"),
            PdbEntryInfo("5a39", "Structure of Rad14 in complex with cisplatin containing DNA"),
            PdbEntryInfo("5djl", "Structure of WT Human Glutathione Transferase in complex with cisplatin in the presence of glutathione."),
            PdbEntryInfo("5my1", "E. coli expressome"),
            PdbEntryInfo("6bs1", "Crystal Structure of Human DNA polymerase kappa in complex with DNA containing the major cisplatin lesion"),
            PdbEntryInfo("6ki6", "Crystal structure of BCL11A in complex with gamma-globin -115 HPFH region"),
            PdbEntryInfo("6m97", "Crystal structure of the high-affinity copper transporter Ctr1"),
            PdbEntryInfo("6wlb", "Structure of homotrimeric poplar cellulose synthase isoform 8"),
            PdbEntryInfo("6xk0", "Albumin-dexamethasone complex"),
            PdbEntryInfo("6zdh", "SARS-CoV-2 Spike glycoprotein in complex with a neutralizing antibody EY6A Fab"),
            PdbEntryInfo("6zxn", "Cryo-EM structure of the SARS-CoV-2 spike protein bound to neutralizing nanobodies (Ty1)"),
            PdbEntryInfo("7c2l", "S protein of SARS-CoV-2 in complex bound with 4A8"),
            PdbEntryInfo("7cwn", "P17-H014 Fab cocktail in complex with SARS-CoV-2 spike protein"),
            PdbEntryInfo("7cwu", "SARS-CoV-2 spike proteins trimer in complex with P17 and FC05 Fabs cocktail"),
            PdbEntryInfo("7k43", "SARS-CoV-2 spike in complex with the S2M11 neutralizing antibody Fab fragment"),
            PdbEntryInfo("7k8t", "Structure of the SARS-CoV-2 S 6P trimer in complex with the human neutralizing antibody Fab fragment, C002 (State 2)"),
            PdbEntryInfo("7kkl", "SARS-CoV-2 Spike in complex with neutralizing nanobody mNb6"),
            PdbEntryInfo("7l06", "Cryo-EM structure of SARS-CoV-2 2P S ectodomain bound to two copies of domain-swapped antibody 2G12"),

    )
}
