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

@file:Suppress("UnnecessaryVariable", "unused", "UNUSED_VARIABLE")

package com.bammellab.mollib.data

import java.util.*

/**
 * PDB reference list
 */

object Corpus {

    /*
     * NB - base of list is 1!!
     */

    fun motmImageListGet(index: Int): String {
        if (index > motmThumbnailImageList.size-1 || index < 0) return "INVALIDINDEX"
        return (motmThumbnailImageList[index])
    }


    val motmThumbnailImageList = listOf(

            "1-Myoglobin-geis-0218-myoglobin-tn.png",
            "2-1cd3_mature-tn.png",
            "3-1tau-tn.png",
            "4-collagen-tn.png",
            "5-1oco-tn.png",
            "6-3hvp-tn.png",
            "7-1aoi-tn.png",
            "8-ecorI-tn.png",
            "9-2lyz-tn.png",
            "10-ribosome-tn.png",
            "11-1rcx-tn.png",
            "12-pepsin-pepsinogen-tn.png",
            "13-1htb-tn.png",
            "14-Insulin-4ins-tn.png",
            "15-4tna-figure-tn.png",
            "16-1asz-tn.png",
            "17-1prh-figure-tn.png",
            "18-1b7t-tn.png",
            "19-Actin-actin18-tn.png",
            "20-picornaviruses-tn.png",
            "21-1igt-tn.png",
            "22-1jb0-tn.png",
            "23-B-DNA-tn.png",
            "24-6gpb-2views-tn.png",
            "25-Thrombin-1ppb-transparent-tn.png",
            "26-1n2c-tn.png",
            "27-1fbb-twoviews-tn.png",
            "28-anthrax-toxin-tn.png",
            "29-3pte-tn.png",
            "30-1fpy-twoviews-tn.png",
            "31-p53-unbound-tn.png",
            "32-chaperonin-tn.png",
            "33-3hvt-tn.png",
            "34-7dfr-tn.png",
            "35-ferritin-tn.png",
            "36-3cyt-twoviews-tn.png",
            "37-1e7i-tn.png",
            "38-1bl8-tn.png",
            "39-lac-tn.png",
            "40-1i6h-composite-tn.png",
            "41-2dhb-tn.png",
            "42-1gfl-tn.png",
            "43-2src-tn.png",
            "44-3cln-tn.png",
            "45-1hcq-1a52-tn.png",
            "46-serine-proteases-tn.png",
            "47-1sva_DNA-tn.png",
            "48-1cgp-tn.png",
            "49-carbonic-anhydrases-tn.png",
            "50-glycolysis-tn.png",
            "51-1eul-membrane-tn.png",
            "52-hormones-tn.png",
            "53-1psi-tn.png",
            "54-1acj-tn.png",
            "55-1a0i-1dgs-tn.png",
            "56-caspases-tn.png",
            "57-7cat-tn.png",
            "58-1gg2-composite-tn.png",
            "59-1s5l-membrane-tn.png",
            "60-ubiquitin-tn.png",
            "61-2pah-1phz-tn.png",
            "62-1hsa-tn.png",
            "63-1tcr-tn.png",
            "64-3kin-composite-tn.png",
            "65-1u6b-tn.png",
            "66-2biw-composite-tn.png",
            "67-1cdw-tn.png",
            "68-1bet-tn.png",
            "69-1xtc-tn.png",
            "70-1qys-composite-tn.png",
            "71-2bg9-composite-tn.png",
            "72-ATPsynthase-tn.png",
            "73-1a36-tn.png",
            "74-1ppi-tn.png",
            "75-2hft-tn.png",
            "76-1ruz-tn.png",
            "77-1gpe-tn.png",
            "78-2d1s-tn.png",
            "79-app-composite-tn.png",
            "80-1yyf-tn.png",
            "81-1ttt-tn.png",
            "82-1w0e-2j0d-tn.png",
            "83-1m1j-composite-tn.png",
            "84-1muh-tube-tn.png",
            "85-1qgk-composite-tn.png",
            "86-2nn6-tn.png",
            "87-1tf6-1un6-tn.png",
            "88-1xi4-tn.png",
            "89-2b3y-2ipy-tn.png",
            "90-fas-tn.png",
            "91-1n4e-tn.png",
            "92-1d2s-composite-tn.png",
            "93-pdb93_1cts-2cts-tn.png",
            "94-Superoxide_Dismutase-94-SuperoxideDismutase-2sod-tn.png",
            "95-2onj-tn.png",
            "96-1w6k-tn.png",
            "97-Kai-proteins-tn.png",
            "98-siRNA-tn.png",
            "99-1l3w-1i7x-tn.png",
            "100-2rh1-tn.png",
            "101-PrP-tn.png",
            "102-3ldh-tn.png",
            "103-1k4r-1ok8-tn.png",
            "104-3bc8_2yye-tn.png",
            "105-5rsa-tn.png",
            "106-1fa0-tn.png",
            "107-2oau_composite_400-tn.png",
            "108-2cg9_composite-tn.png",
            "109-2tmv_composite-tn.png",
            "110-2p1p-tn.png",
            "111-hydrogenases-tn.png",
            "112-1gt0_composite-tn.png",
            "113-Influenza_Neuraminidase-neuraminidases-tn.png",
            "114-vaults-tn.png",
            "115-1sgz_1py1-tn.png",
            "116-sulfotransferases-tn.png",
            "117-1fo4_composite-tn.png",
            "118-2zxe_composite-tn.png",
            "119-lattice-tn.png",
            "120-1kdf-tn.png",
            "121-2wdk_2wdl_front-tn.png",
            "122-enhanceosome-tn.png",
            "123-3g61_composite-tn.png",
            "124-1cvn-tn.png",
            "125-1fpv_2cas-tn.png",
            "126-mom126_egfr-tn.png",
            "127-mom127_crystallins2-tn.png",
            "128-mom128_interferons-tn.png",
            "129-pdb129-3blw_9icd-tn.png",
            "130-mom130-1u8d-tn.png",
            "131-1jva-tn.png",
            "132-Adenovirus_adenovirus-tn.png",
            "133-NitricOxideSynthase_nNOS-tn.png",
            "134-mom134_1jv2-2k9j-1m8o-tn.png",
            "135-mom135_3os1_composite-tn.png",
            "136-Nanobodies_antibody_nanobody-tn.png",
            "137-Cytochromebc1_3h1j-tn.png",
            "138-Glucansucrase_3aic-tn.png",
            "139-DNAMethylases_DNMT-tn.png",
            "140-RhomboidProteaseGlpG_2nrf_membrane-tn.png",
            "141-OGlcNAcTransferase_3pe4_1w3b-tn.png",
            "142-PDBPioneers_oxygencarrier-tn.png",
            "143-TollLikeReceptors_3fxi_2j67-tn.png",
            "144-ComplexI_3m9s_3rko-tn.png",
            "145-MessengerRNACapping_3kyh_1ri1-tn.png",
            "146-AminoglycosideAntibiotics_3frh_3pb3-tn.png",
            "147-Rhodopsin_1f88-tn.png",
            "148-RasProtein_5p21-tn.png",
            "149-Leptin_1ax8-tn.png",
            "150-SlidingClamps_3u5z_open-tn.png",
            "151-HGPRT_1hmp-tn.png",
            "152-cAMP-dependentProteinKinase_PKA-tn.png",
            "153-PyruvateDehydrogenaseComplex_pyruvatedehydrogenase-tn.png",
            "154-CitricAcidCycle_citricacidcycle-tn.png",
            "155-VitaminDReceptor_VDRcryo-tn.png",
            "156-ABOBloodTypeGlycosyltransferases_3i0g_composite-tn.png",
            "157-TransferMessengerRNA_3iyr_composite-tn.png",
            "158-ProtonGatedUreaChannel_3ux4-tn.png",
            "159-Erythrocruorin_2gtl-tn.png",
            "160-Actinomycin_173d_composite-tn.png",
            "161-Ricin_2aai-tn.png",
            "162-Dermcidin_2ymk_twoviews-tn.png",
            "163-HIVCapsid_capsid_composite-tn.png",
            "164-SerotoninReceptor_4iar-tn.png",
            "165-DesignedProteinCages_3vdx_4egg-tn.png",
            "166-Proteasome_4b4t_dimer-tn.png",
            "167-SNAREProteins_1sfc_1br0-tn.png",
            "168-DNAHelicase_4esv-tn.png",
            "169-HIVEnvelopeGlycoprotein_4nco_composite-tn.png",
            "170-BroadlyNeutralizingAntibodies_4nco_bnAb-tn.png",
            "171-NeurotransmitterTransporters_4m48-tn.png",
            "172-RecAAndRad51_recA_filament-tn.png",
            "173-Aquaporin_1fqy-tn.png",
            "174-GFPLikeProteins_1g7k-tn.png",
            "175-Microtubules_3j2u-tn.png",
            "176-Dynein_3vkh-label-tn.png",
            "177-Apoptosomes_human_apoptosome-tn.png",
            "178-EbolaVirusProteins_EbolaProteins-tn.png",
            "179-MethylCoenzymeMReductase_1mro-tn.png",
            "180-TALEffectors_3ugm-tn.png",
            "181-CascadeAndCRISPR_1vy8-tn.png",
            "182-InsulinReceptor_insulinreceptor-tn.png",
            "183-Phototropin_phototropin-tn.png",
            "184-Glucagon_glucagonreceptor-tn.png",
            "185-Titin_sarcomere_titin-tn.png",
            "186-ReceptorForAdvancedGlycationEndProducts_4lp5_figure-tn.png",
            "187-New_Delhi_MetalloBetaLactamase-4eyl-tn.png",
            "188-TetrahydrobiopterinBiosynthesis_1gtp_1gtq_1sep-tn.png",
            "189-Amyloids_2m4j-tn.png",
            "190-TwoComponentSystems_2c2a_3by8-tn.png",
            "191-GlutamateGatedChlorideReceptors_3rif-tn.png",
            "192-Vancomycin-1fvm-tn.png",
            "193-Siderocalin-3cmp_composite-tn.png",
            "194-Designer_Insulins-1trz-tn.png",
            "195-Raf_Protein_Kinases-raf-tn.png",
            "196-Lead_Poisoning-1qnv-tn.png",
            "197-Zika_Virus-5ire_glyc-tn.png",
            "198-betaGalactosidase-1jz8-tn.png",
            "199-Monellin-SuperSweetHomepage-tn.png",
            "200-Quasisymmetry_in_Icosahedral_Viruses-1ohf_homepage2-tn.png",
            "201-Isoprene_Synthase-3n0g_homepage-tn.png",
            "202-Dipeptidyl_Peptidase4-1nu8_homepage-tn.png",
            // END funkyness

            "203-Aminopeptidase_1_and_Autophagy-4r8f_homepage-tn.png",

            "204-PD1_Programmed_Cell_Death_Protein_1-203_homepage-tn.png",


            // note: 10/2020 - still swapped.
            // START: note that 203/204 is funky - They are SWAPPED.   And has stayed that way.
            //   unswapped here

            "205-Nuclear_Pore_Complex-NPC_homepage-tn.png",
            "206-Globin_Evolution-Globin_Homepage-tn.png",
            "207-Photoactive_Yellow_Protein-2pyp_homepage-tn.png",
            "208-Glucose_Transporters-4pyp_4zwc_homepage-tn.png",
            "209-Tissue_Transglutaminase_and_Celiac_Disease-2q3z_3ly6_homepage-tn.png",
            "210-Adenine_Riboswitch_in_Action-5e54_5swe_homepage-tn.png",
            "211-Pilus_Machine-3jc8_homepage-tn.png",
            "212-Glutathione_Transferases-3gss_homepage-tn.png",
            "213-Sirtuins-4iao_homepage-tn.png",
            "214-Chimeric_Antigen_Receptors-CAR_homepage-tn.png",
            "215-Aspartate_Transcarbamoylase-5at1_homepage-tn.png",
            "216-Biodegradable_Plastic-5t6o_homepage-tn.png",
            "217-Opioid_Receptors-4dkl-homepage-tn.png",
            "218-EPSP_Synthase_and_Weedkillers-2gg4_2gga_homepage-tn.png",
            "219-Vacuolar_ATPase-5vox_homepage-tn.png",
            "220-Dehalogenases-4ur0-homepage-tn.png",
            "221-Proteins_and_Nanoparticles-5et3_homepage-tn.png",
            "222-Human_Papillomavirus_and_Vaccines-6bt3-homepage2-tn.png",
            "223-Piezo1_Mechanosensitive_Channel-6b3r_homepage-tn.png",
            "224-Legumain-4awb_homepage-tn.png",
            "225-Phytase-1dkq_homepage-tn.png",
            "226-Aminoglycoside_Antibiotics_and_Resistance-1fjg_homepage-tn.png",
            "227-Telomerase-6d6v-homepage-tn.png",
            "228-Directed_Evolution_of_Enzymes-5ucw_homepage-tn.png",
            "229-Fluorescent_RNA_AptamersSpinach_fluorescent_aptamer_with_RNA_in_light_orange_and_fluorophore_in_green-4kzd_homepage-tn.png",
            "230-Initiation_Factor_eIF4E-1rf8_homepage-tn.png",
            "231-Proteins_and_Biominerals-1q8h_homepage-tn.png",
            "232-Measles_Virus_Proteins-4uft_homepage-tn.png",
            "233-SNitrosylated_Hemoglobin-1buw_homepage-tn.png",
            "234-MDM2_and_Cancer-MDM2_homepage-tn.png",

// update 10/2020 (above)(imagesProcessing/ from motm-by-date.  natural order.

            "235-AMPA_Receptor-3kg2_homepage-tn.png",
            "236-Cyclin_and_Cyclindependent_Kinase-1fin_homepage-tn.png",
            "237-Nanodiscs_and_HDL-6clz_homepage-tn.png",
            "238-Ribonucleotide_Reductase-1mrr_3r1r_homepage-tn.png",
            "239-Phospholipase_A2-1bp2_homepage-tn.png",
            "240-HypoxiaInducible_Factors-1lqb_homepage-tn.png",
            "241-Twenty_Years_of_Molecules-6j4y_homepage-tn.png",
//            "243-Coronavirus_Proteases-6lu7_homepage-tn.png",
//            "242-Voltagegated_Sodium_Channels-6j8j_homepage-tn.png",

            "242-Coronavirus_Proteases-6lu7_homepage-tn.png",
            "243-Voltagegated_Sodium_Channels-6j8j_homepage-tn.png",

            // 243/242 are swapped??  - reverse this or it messes up the screensaver
            "244-Photosynthetic_Supercomplexes-5xnl_homepage-tn.png",
            "245-Spliceosomes-3jb9_homepage-tn.png",
            "246-Coronavirus_Spike-6crz_6vxx_homepage2-tn.png",
            "247-Myelinassociated_Glycoprotein-5lf5_homepage3-tn.png",
            "248-Phytosulfokine_Receptor-4z64_homepage-tn.png",
            "249-SARSCoV2_RNAdependent_RNA_Polymerase-6yyt_homepage-tn.png",
            "250-Capsaicin_Receptor_TRPV1-5is0_homepage-tn.png",
            "251-Adenylyl_Cyclase-6r3q_homepage-tn.png",
            "252-Hepatitis_C_Virus_ProteaseHelicase-1cu1_homepage-tn.png",
            "253-Expressome-6x9q_homepage-tn.png",
            "254-Cellulose_Synthase-6wlb_composite.png",
            "255-Cisplatin_and_DNA-Fig1.png",
            "256-SARSCoV2_Spike_and_Antibodies-Spike_Antibodies.png",
            "257-Fetal_Hemoglobin-1fdh_4hhb.png"

    )


    fun motmTagLinesGet(index: Int): String {
        val size = motmTagLines.size
        if (index > motmTagLines.size-1 || index < 0) return "INVALIDINDEX"
        return (motmTagLines[index])
    }

    // fgrep the <p> from motm-by-date, then manual edit, reverse with :g/^/m0

    private val motmTagLines = arrayOf(
            "Myoglobin was the first protein to have its atomic structure determined, revealing how it stores oxygen in muscle cells.",
            "Bacteriophage phiX174 hijacks bacterial cells and forces them to make new copies of the virus",
            "DNA polymerase makes an accurate copy of the cell's genome",
            "Sturdy fibers of collagen give structure to our bodies",
            "Cytochrome oxidase extracts energy from food using oxygen",
            "Atomic structures of HIV protease have been used to design powerful drugs for HIV therapy",
            "The cell's genome is stored and by nucleosomes",
            "Bacterial enzymes that cut DNA are useful tools for genetic engineering",
            "Lysozyme attacks the cell walls of bacteria",
            "Atomic structures of the ribosomal subunits reveal a central role for RNA in protein synthesis",
            "Rubisco fixes atmospheric carbon dioxide into bioavailable sugar molecules",
            "Pepsin digests proteins in strong stomach acid",
            "Alcohol dehydrogenase detoxifies the ethanol we drink",
            "The hormone insulin helps control the level of glucose in the blood",
            "Transfer RNA translates the language of the genome into the language of proteins",
            "Aminoacyl-tRNA synthetases ensure that the proper amino acids are used to build proteins",
            "Aspirin attacks an important enzyme in pain signaling and blood clotting",
            "Molecular motors fueled by ATP power the contraction of muscles",
            "Cells are supported by a cytoskeleton of protein filaments",
            "Crystallographic structures reveal the atomic details of viruses and how to fight them",
            "Antibodies search for foreign molecules in the blood",
            "Photosystem I captures the energy in sunlight",
            "Atomic structures reveal how the iconic double helix encodes genomic information",
            "Glycogen phosphorylase releases sugar from its cellular storehouse",
            "Thrombin activates the molecule that forms blood clots",
            "Nitrogenase uses an exotic cluster of metals to fix atmospheric nitrogen into bioavailable ammonia",
            "Bacteriorhodopsin pumps protons powered by green sunlight",
            "Anthrax bacteria build a deadly three-part toxin",
            "Penicillin attacks the proteins that build bacterial cell walls",
            "Glutamine synthetase monitors the levels of nitrogen-rich amino acids and decides when to make more",
            "p53 tumor suppressor protects the body from DNA damage and cancer",
            "Chaperones help new proteins fold into their proper shape",
            "HIV builds a DNA copy of its RNA genome, providing a unique target for drug therapy",
            "DHFR is a target for cancer chemotherapy and bacterial infection",
            "Ferritin and transferrin manage our essential stores of iron ions",
            "Cytochrome c shuttles electrons during the production of cellular energy",
            "Serum albumin delivers fatty acid molecules through the bloodstream",
            "Potassium channels allow potassium ions to pass, but block smaller sodium ions",
            "A genetic circuit controls the production of lactose-utilizing enzymes in bacteria",
            "RNA polymerase transcribes genetic information from DNA into RNA",
            "Hemoglobin uses a change in shape to increase the efficiency of oxygen transport",
            "A tiny fluorescent protein from jellyfish has revolutionized cell biology",
            "Growth signaling proteins play an important role in the development of cancer",
            "Calcium ions rapidly deliver signals to control processes such as muscle contraction, nerve signaling, and fertilization",
            "Estrogen binds to receptors in the nucleus and affects key genes in development",
            "An activated serine amino acid in trypsin cleaves protein chains ",
            "SV40 hijacks the cells it infects using only a handful of proteins",
            "CAP senses the level of sugar and mobilizes the proteins needed to utilize it",
            "Carbonic anhydrase solubilizes carbon dioxide gas so we can breathe it out",
            "The ten enzymes of glycolysis break down sugar in our diet",
            "Atomic structures have captured the calcium pump in action",
            "Growth hormone brings together two copies of its cellular receptor",
            "Serpins are traps that capture dangerous proteases",
            "Acetylcholinesterase stops the signal between a nerve cell and a muscle cell",
            "DNA ligase reconnects broken DNA strands, and is used to engineer recombinant DNA",
            "Caspases disassemble proteins during the process of programmed cell death",
            "Catalase protects us from dangerous reactive oxidizing molecules",
            "Trimeric G-proteins receive signals from cellular receptors and deliver them inside the cell",
            "Photosystem II captures the energy from sunlight and uses it to extract electrons from water molecules",
            "Ubiquitin is used to tag obsolete proteins for destruction",
            "An unusual cofactor is used in the synthesis of aromatic amino acids",
            "MHC displays peptides on the surfaces of cells, allowing the immune system to sense the infection inside",
            "Lymphocytes use T-cell receptors to patrol the body for foreign molecules",
            "The motor protein kinesin carries cellular cargo along microtubules",
            "Special sequences of RNA are able to splice themselves",
            "Light-sensing retinal molecules are built from colorful carotenoids in our diet",
            "TATA protein tells RNA polymerase where to get started on a gene",
            "Neurotrophins guide the development of the nervous system",
            "Many bacterial toxins have two parts: one that finds a cell, the other that kills it",
            "Researchers have successfully designed entirely new proteins based on biological principles",
            "The neurotransmitter acetylcholine opens a protein channel, stimulating muscle contraction",
            "ATP synthase links two rotary motors to generate ATP",
            "Topoisomerases untangle and reduce the tension of DNA strands in the cell",
            "Amylases digest starch to produce glucose",
            "Tissue factor senses damage to the body and triggers formation of a blood clot",
            "Influenza virus binds to cells and infects them using hemagglutinin",
            "Glucose oxidase measures blood glucose level in biosensors",
            "Organisms from fireflies to bacteria use luciferase to emit light",
            "Cell-clogging amyloids form when proteins improperly aggregate",
            "AAA+ proteases are ATP-powered molecular motors that thread protein chains through a hole ",
            "Protein synthesis requires the assistance of several elongation factors that guide each step",
            "Cytochrome p450 detoxifies and solubilizes drugs and poisons by modifying them with oxygen",
            "Rod-shaped fibrin molecules link together to form blood clots",
            "Transposases shift genes around in the genome",
            "Importins deliver proteins into the nucleus through the nuclear pore complex",
            "Exosomes destroy messenger RNA molecules after they have finished their jobs",
            "Zinc ions are used to strengthen small protein modules that recognize DNA",
            "Three-armed clathrin triskelions are used to build molecular cages involved in transport",
            "Aconitase performs a reaction in the citric acid cycle, and moonlights as a regulatory protein",
            "Fatty acids are constructed in many sequential steps by a large protein complex",
            "Ultraviolet light damages our DNA, but our cells have ways to correct the damage",
            "Anabolic steroids like testosterone are among the most common performance enhancing drugs",
            "Citrate synthase opens and closes around its substrates as part of the citric acid cycle",
            "Superoxide dismutase protects us from dangerously reactive forms of oxygen",
            "Many bacteria use multidrug resistance transporters to pump drugs and poisons out of the cell",
            "Oxidosqualine cyclase forms the unusual fused rings of cholesterol molecules",
            "Circadian clock proteins measure time in our cells",
            "Our cells continually look for pieces of double-stranded RNA, a possible sign of viral infection",
            "Adhesive cadherin proteins hold neighboring cells together",
            "Adrenaline stimulates a G-protein-coupled receptor, priming us for action",
            "Mad cow disease is caused by prion proteins that misfold and aggregate",
            "Our cells temporarily build lactate when supplies of oxygen are low",
            "Atomic structures of dengue virus are giving new hope for creation of a vaccine",
            "Selenium is used in place of sulfur to build proteins for special tasks",
            "Ribonuclease cuts and controls RNA",
            "Poly(A) polymerase adds a long tail of adenine nucleotides at the end of messenger RNA",
            "Pressure-sensitive channels open when the internal pressure of a cell gets too high",
            "Heat shock proteins ensure that proteins remain folded and active under harsh conditions",
            "A cylindrical arrangement of proteins protects a long strand of RNA in TMV",
            "The plant hormone auxin controls growth and response to light and gravity",
            "Hydrogenases use unusual metal ions to split hydrogen gas",
            "Transcription factors decide when particular genes will be transcribed",
            "Neuraminidase is an important target for influenza drugs",
            "Cells build huge vault containers constructed of a symmetric arrangement of many subunits",
            "Beta-secretase trims proteins in the cell and plays an important role in Alzheimer's disease",
            "Sulfotransferases transfer sulfuryl groups in enzymatic reactions ",
            "Xanthine oxidoreductase helps break down obsolete purine nucleotides",
            "Cells continually pump sodium ions out and potassium ions in, powered by ATP",
            "Small pieces of DNA have been engineered to form a nanoscale lattice",
            "Small antifreeze proteins protect cells from damage by ice",
            "Ribosomes are complex molecular machines that build proteins",
            "Enhanceosomes help decide the appropriate time to transcribe a gene",
            "P-glycoprotein pumps toxic molecules out of our cells",
            "For some proteins, clipped and reassembled sequences can produce the same 3D shape",
            "Viruses that cause distemper are surrounded by an icosahedral capsid",
            "EGF is part of a family of proteins that controls aspects of cell growth and development",
            "A concentrated solution of crystallins refracts light in our eye lens",
            "Interferons mobilize defenses against viral infection",
            "Atomic structures have revealed the catalytic steps of a citric acid cycle enzyme",
            "Special sequences of messenger RNA can bind to regulatory molecules and affect synthesis of proteins",
            "Inteins splice themselves out of larger protein chains",
            "Adenovirus has an icosahedral capsid with unusual cell-binding fibers",
            "Nitric oxide gas is used as a rapid-acting hormone and as a powerful defense ",
            "Integrins forge flexible linkages between the infrastructure inside and outside of cells",
            "HIV integrase allows HIV to insert itself into the genome of an infected cell",
            "Unusual antibodies from camels are useful in research and medicine",
            "A flow of electrons powers proton pumps in cellular respiration and photosynthesis",
            "Bacteria adhere to our teeth by building sticky sugar chains",
            "Cells add methyl groups to their DNA to encode additional epigenetic information",
            "Some proteases cut proteins embedded in cell membranes",
            "Some protein functions are regulated when sugars are attached",
            "A dozen historic structures set the foundation for the PDB archive",
            "Toll-like receptors warn us about bacterial and viral infection",
            "A proton-pumping protein complex performs the first step of the respiratory electron transport chain",
            "Messenger RNA molecules are capped with an inverted nucleotide",
            "Antibiotic-resistant bacteria build enzymes that destroy drugs like streptomycin",
            "In our eyes, rhodopsin uses the molecule retinal to see light",
            "Mutation of the growth-contolling ras protein can lead to cancer",
            "Problems with the appetite-controlling hormone leptin can lead to obesity",
            "Sliding clamps slide along DNA strands and keep DNA polymerase on track during replication",
            "Cells salvage and recycle their obsolete DNA and RNA",
            "PKA delivers cellular signals by adding phosphates to proteins",
            "A huge molecular complex links three sequential reactions for energy production",
            "Eight enzymes form a cyclic pathway for energy production and biosynthesis",
            "Vitamin D helps regulate the use of calcium throughout the body",
            "ABO blood types are determined by an enzyme that attaches sugars to proteins",
            "tmRNA rescues ribosomes that are stalled by damaged messenger RNA",
            "A channel that passes urea allows ulcer-producing bacteria to live in the stomach",
            "Earthworms build a huge version of hemoglobin to carry oxygen",
            "Some antibiotics attack cells by intercalating between the bases in a DNA double helix",
            "The structure of ricin reveals how it kills cells and how vaccines can produce immunity against ricin poisoning",
            "Small toxic peptides help protect us from bacterial infection",
            "At the center of HIV, an unusual cone-shaped capsid protects the viral genome and delivers it into infected cells",
            "Serotonin receptors control mood, emotion, and many other behaviors, and are targets for many important drugs",
            "Researchers are modifying natural proteins to design new self-assembling protein cages",
            "Proteasomes destroy damaged or obsolete proteins inside cells",
            "SNARE proteins power the fusion of vesicles with membranes by forming a bundle of alpha helices",
            "DNA helicase pries apart the two strands in a DNA double helix, powered by ATP",
            "Envelope protein attaches HIV to the cells that it infects and powers fusion of the virus with the cell membrane",
            "Structural studies of broadly neutralizing antibodies are paving the way to vaccines for HIV, influenza and RSV",
            "Neurotransmitters are transported out of nerve synapses to end a signal transmission",
            "Broken DNA strands may be repaired by matching sequences in a duplicate copy of the DNA",
            "Aquaporins create a channel for water molecules to cross through cell membranes",
            "GFP-like proteins found in nature or engineered in the laboratory now span every color of the rainbow ",
            "The largest filaments of the cytoskeleton provide tracks for transport throughout the cell",
            "The motor protein dynein transports cellular cargo along microtubules",
            "Apoptosomes make life or death decisions in cells",
            "Structures of ebola virus proteins are giving new hope for fighting this deadly virus",
            "Methanogens use sophisticated molecular tools to build methane",
            "TAL-effectors are modular, DNA-reading proteins that can be used to edit DNA in living cells",
            "Cascade and CRISPR help bacteria remember how to fight viral infection",
            "The cellular receptor for insulin helps control the utilization of glucose by cells",
            "Phototrophins sense the level of blue light, allowing plants to respond to changing environmental conditions",
            "Glucagon triggers the release of glucose into the blood, to power cells throughout the body",
            "The giant protein titin organizes the structure of muscle and gives them elasticity",
            "RAGE recognizes sugar-modified proteins, contributing to an inflammatory response that plays a role in diabetes",
            "Antibiotics can save lives, but antibiotic-resistant strains of bacteria pose a dangerous threat",
            "Tetrahydrobiopterin plays an essential role in the production of aromatic amino acids, neurotransmitters and nitric oxide.",
            "Alzheimer's disease and prion diseases are linked to unnatural aggregation of proteins into amyloid fibrils.",
            "Bacteria respond to their environment with two-component sensing systems.",
            "The antibiotic ivermectin attacks glutamate-gated chloride channels, paralyzing parasitic worms.",
            "The antibiotic vancomycin blocks the construction of bacterial cell walls.",
            "Our innate immune system starves bacteria of iron using siderocalin.",
            "Engineered insulins have been developed to improve treatment of diabetes",
            "A single mutation in a RAF protein kinase can help transform a normal cell into a cancer cell.",
            "Lead ions poison proteins throughout the body, blocking their normal function.",
            "Cryo-electron microscopy reveals the structure of Zika virus",
            "Beta-galactosidase is a powerful tool for genetic engineering of bacteria",
            "Monellin and other supersweet proteins trick our taste receptors.",
            "Viruses use quasisymmetry to build large capsids out of many small subunits",
            "Plants release a billion metric tons of isoprene and other organic gases every year",
            "Inhibitors of dipeptidyl peptidase 4 are used to treat type-2 diabetes",
            "Aminopeptidase 1 is delivered inside the cell using the machinery of autophagy",


            // UPDATE was here and above

            "PD-1 and its ligands are a new target for cancer therapy",
            "The nuclear pore complex is the gateway between the nucleus and cytoplasm.",
            "The mechanisms of molecular evolution are revealed in globin sequences and structures.",
            "Researchers use synchrotrons and X-ray lasers to reveal the rapid processes of light sensing.",
            "Glucose transporters deliver glucose molecules one-by-one across cell membranes.",
            "Tissue transglutaminase staples proteins together by forming a chemical crosslink.",
            "XFEL serial crystallography reveals what happens when adenine binds to a riboswitch",
            "A molecular machine with a rotary motor builds a long protein filament involved in bacterial motility and attachment.",
            "Glutathione transferase tags toxic molecules, making them easy to recognize and remove.",
            "Sirtuin activation is being explored as a way to slow aging.",
            "T cells may be engineered with chimeric antigen receptors to attack cancer cells.",
            "Key biosynthetic enzymes are regulated by their ultimate products through allosteric motions.",
            "Bacteria build biodegradable plastic that could be better for the environment",
            "Morphine and other opioid drugs bind to receptors in the nervous system, controlling pain",
            "The weedkiller Roundup attacks a key enzyme involved in the construction of aromatic compounds.",
            "Two linked molecular motors are used to pump protons across membranes.",
            "Bacteria destroy toxic environmental pollutants that include chlorine or bromine atoms.",
            "The capsid protein of papillomavirus is used in vaccines that prevent cervical cancer.",
            "Nanotech scientists are designing new ways to combine proteins and nanoparticles ",
            "Mechanosensitive ion channels give our cells a sense of touch.",
            "Legumain cleaves proteins, and can also put them back together.",
            "Phytase is used in agriculture to mobilize indigestible phosphate compounds in livestock feed.",
            "Bacteria become resistant to aminoglycosides by destroying them or changing their target.",
            "Telomerase maintains the ends of our chromosomes.",
            "Biological evolution is being harnessed in the lab to create new enzymes.",
            "RNA aptamers are being engineered to track molecules inside living cells", //January 2019

            "Initiation factors for protein synthesis interact through disordered chains.",//February 2019

            "Six proteins in measles virus work together to infect cells.",//March 2019

            "Small biomineral crystals are used to build bone, eggshells and even tiny compasses.",//April 2019

            "Nitric oxide is attached to a conserved cysteine in hemoglobin and then released to control the flow of blood.",
//May 2019
            "MDM2 controls the action of p53 tumor suppressor, making it a target for cancer chemotherapy.",
//June 2019
            "Receptors for the neurotransmitter glutamate in our brain come in several shapes and sizes.",
//July 2019
            "Cyclins and cyclin-dependent kinases control when cells divide, making them important targets for cancer therapy.",
//August 2019
            "Nanodiscs conveniently package a small piece of membrane for experimental studies.",
//September 2019
            "Ribonucleotide reductase creates the building blocks of DNA",
//October 2019
            "Phospholipase A2 breaks membrane lipids, forming molecules that contribute to inflammation and pain signaling.",
//November 2019
            "HIF-Î± is a molecular switch that responds to changing oxygen levels.",
//December 2019
            "Celebrating the structural biology revolution",
//January 2020
            "Coronavirus proteases are attractive targets for the design of antiviral drugs.",
//February 2020
            "Voltage-gated sodium channels transmit signals in a wave through the nervous system.",
//March 2020
            "Light is captured by huge supercomplexes of photosystems and antenna systems.",
//April 2020
            "Cryoelectron microscropy is revealing how spliceosomes cut-and-paste messenger RNA molecules.",
//May 2020
            "Coronavirus spike protein binds to receptors on cell surfaces, and is a target for vaccine development.",
//June 2020
            "Nerve axons are insulated and protected by a sheath of myelin",
//July 2020
            "Phytosulfokine and other small peptides deliver signals about growth and development in plants.",
//August 2020
            "Coronaviruses duplicate their RNA genome using a specialized polymerase",//September 2020

            "TRPV1 is an ion channel that senses heat and contributes to pain sensation.",//October 2020

            "Adenylyl cyclase creates second messengers to amplify signals from G-protein coupled receptors",  // Nov 2020

            "Structures of hepatitis C viral proteins have led to the discovery of direct-acting antivirals.", // Dec 2020

            "In bacteria, ribosomes start building proteins as messenger RNA is being transcribed", // Jan 2021
            "Plants build tough cellulose strands one sugar at a time.",
            "Cisplatin treats cancer by causing damage to the DNA of cancer cells.",
            "Structural biologists are revealing the many ways that antibodies recognize SARS-CoV-2",
            "Fetal hemoglobin allows a growing fetus to receive oxygen from their mother.", // May 2021
            ""
    )

    private val monthNames = arrayOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
    )

    fun motmDateByKey(key: Int): String {
        if (key < 1) return ("INVALID DATE")
        val month = (key - 1) % 12
        val year = (key - 1) / 12 + 2000

        val retString = monthNames[month] + " " + String.format("%4d", year)
        // String.format("%6.2f", elapsed_time);

        return retString
    }

    // extracted via grep from motm-by-date file

    fun motmTitleGet(index: Int): String {
        if (index > corpus.size || index < 1) return "INVALID INDEX"
        return (corpus[index-1])
    }

    fun invertPosition(index: Int): Int {
        if (index > corpus.size || index < 0) return 0
        return (corpus.size - index - 1)
    }

    /*
     * The short month names are used in the fast scroll list of all MotM entries
     */
    private val monthNamesShort = arrayOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
    )


    private fun monthStringByKey(key: Int): String {
        if (key < 1) return ("INVALID DATE")
        val month = (key - 1) % 12
        val year = (key - 1) / 12 + 2000

        val retString = monthNamesShort[month] + " " + String.format("%4d", year)
        // String.format("%6.2f", elapsed_time);

        return retString
    }

    fun generateMonthList(): Array<String> {
        val initList = mutableListOf<String>()

        var j = 0
        for (i in numMonths downTo 1) {
            initList.add(monthStringByKey(i))
        }
        return initList.toTypedArray()
    }

    private const val numMonths = 253

    // grep the href in the motm-by-date file

    val corpus = listOf(
            /* //motm/1 */ "Myoglobin",
            /* //motm/2 */ "Bacteriophage phiX174",
            /* //motm/3 */ "DNA Polymerase",
            /* //motm/4 */ "Collagen",
            /* //motm/5 */ "Cytochrome c Oxidase",
            /* //motm/6 */ "HIV-1 Protease",
            /* //motm/7 */ "Nucleosome",
            /* //motm/8 */ "Restriction Enzymes",
            /* //motm/9 */ "Lysozyme",
            /* //motm/10 */ "Ribosomal Subunits",
            /* //motm/11 */ "Rubisco",
            /* //motm/12 */ "Pepsin",
            /* //motm/13 */ "Alcohol Dehydrogenase",
            /* //motm/14 */ "Insulin",
            /* //motm/15 */ "Transfer RNA",
            /* //motm/16 */ "Aminoacyl-tRNA Synthetases",
            /* //motm/17 */ "Cyclooxygenase",
            /* //motm/18 */ "Myosin",
            /* //motm/19 */ "Actin",
            /* //motm/20 */ "Poliovirus and Rhinovirus",
            /* //motm/21 */ "Antibodies",
            /* //motm/22 */ "Photosystem I",
            /* //motm/23 */ "DNA",
            /* //motm/24 */ "Glycogen Phosphorylase",
            /* //motm/25 */ "Thrombin",
            /* //motm/26 */ "Nitrogenase",
            /* //motm/27 */ "Bacteriorhodopsin",
            /* //motm/28 */ "Anthrax Toxin",
            /* //motm/29 */ "Penicillin-binding Proteins",
            /* //motm/30 */ "Glutamine Synthetase",
            /* //motm/31 */ "p53 Tumor Suppressor",
            /* //motm/32 */ "Chaperones",
            /* //motm/33 */ "HIV Reverse Transcriptase",
            /* //motm/34 */ "Dihydrofolate Reductase",
            /* //motm/35 */ "Ferritin and Transferrin",
            /* //motm/36 */ "Cytochrome c",
            /* //motm/37 */ "Serum Albumin",
            /* //motm/38 */ "Potassium Channels",
            /* //motm/39 */ "lac Repressor",
            /* //motm/40 */ "RNA Polymerase",
            /* //motm/41 */ "Hemoglobin",
            /* //motm/42 */ "Green Fluorescent Protein (GFP)",
            /* //motm/43 */ "Src Tyrosine Kinase",
            /* //motm/44 */ "Calmodulin",
            /* //motm/45 */ "Estrogen Receptor",
            /* //motm/46 */ "Trypsin",
            /* //motm/47 */ "Simian Virus 40",
            /* //motm/48 */ "Catabolite Activator Protein",
            /* //motm/49 */ "Carbonic Anhydrase",
            /* //motm/50 */ "Glycolytic Enzymes",
            /* //motm/51 */ "Calcium Pump",
            /* //motm/52 */ "Growth Hormone",
            /* //motm/53 */ "Serpins",
            /* //motm/54 */ "Acetylcholinesterase",
            /* //motm/55 */ "DNA Ligase",
            /* //motm/56 */ "Caspases",
            /* //motm/57 */ "Catalase",
            /* //motm/58 */ "G Proteins",
            /* //motm/59 */ "Photosystem II",
            /* //motm/60 */ "Ubiquitin",
            /* //motm/61 */ "Phenylalanine Hydroxylase",
            /* //motm/62 */ "Major Histocompatibility Complex",
            /* //motm/63 */ "T-Cell Receptor",
            /* //motm/64 */ "Kinesin",
            /* //motm/65 */ "Self-splicing RNA",
            /* //motm/66 */ "Carotenoid Oxygenase",
            /* //motm/67 */ "TATA-Binding Protein",
            /* //motm/68 */ "Neurotrophins",
            /* //motm/69 */ "Cholera Toxin",
            /* //motm/70 */ "Designer Proteins",
            /* //motm/71 */ "Acetylcholine Receptor",
            /* //motm/72 */ "ATP Synthase",
            /* //motm/73 */ "Topoisomerases",
            /* //motm/74 */ "Alpha-amylase",
            /* //motm/75 */ "Tissue Factor",
            /* //motm/76 */ "Hemagglutinin",
            /* //motm/77 */ "Glucose Oxidase",
            /* //motm/78 */ "Luciferase",
            /* //motm/79 */ "Amyloid-beta Precursor Protein",
            /* //motm/80 */ "AAA+ Proteases",
            /* //motm/81 */ "Elongation Factors",
            /* //motm/82 */ "Cytochrome p450",
            /* //motm/83 */ "Fibrin",
            /* //motm/84 */ "Transposase",
            /* //motm/85 */ "Importins",
            /* //motm/86 */ "Exosomes",
            /* //motm/87 */ "Zinc Fingers",
            /* //motm/88 */ "Clathrin",
            /* //motm/89 */ "Aconitase and Iron Regulatory Protein 1",
            /* //motm/90 */ "Fatty Acid Synthase",
            /* //motm/91 */ "Thymine Dimers",
            /* //motm/92 */ "Anabolic Steroids",
            /* //motm/93 */ "Citrate Synthase",
            /* //motm/94 */ "Superoxide Dismutase",
            /* //motm/95 */ "Multidrug Resistance Transporters",
            /* //motm/96 */ "Oxidosqualene Cyclase",
            /* //motm/97 */ "Circadian Clock Proteins",
            /* //motm/98 */ "Small Interfering RNA (siRNA)",
            /* //motm/99 */ "Cadherin",
            /* //motm/100 */ "Adrenergic Receptors",
            /* //motm/101 */ "Prions",
            /* //motm/102 */ "Lactate Dehydrogenase",
            /* //motm/103 */ "Dengue Virus",
            /* //motm/104 */ "Selenocysteine Synthase",
            /* //motm/105 */ "Ribonuclease A",
            /* //motm/106 */ "Poly(A) Polymerase",
            /* //motm/107 */ "Mechanosensitive Channels",
            /* //motm/108 */ "Hsp90",
            /* //motm/109 */ "Tobacco Mosaic Virus",
            /* //motm/110 */ "Auxin and TIR1 Ubiquitin Ligase",
            /* //motm/111 */ "Hydrogenase",
            /* //motm/112 */ "Oct and Sox Transcription Factors",
            /* //motm/113 */ "Influenza Neuraminidase",
            /* //motm/114 */ "Vaults",
            /* //motm/115 */ "Beta-secretase",
            /* //motm/116 */ "Sulfotransferases",
            /* //motm/117 */ "Xanthine Oxidoreductase",
            /* //motm/118 */ "Sodium-Potassium Pump",
            /* //motm/119 */ "Designed DNA Crystal",
            /* //motm/120 */ "Antifreeze Proteins",
            /* //motm/121 */ "Ribosome",
            /* //motm/122 */ "Enhanceosome",
            /* //motm/123 */ "P-glycoprotein",
            /* //motm/124 */ "Concanavalin A and Circular Permutation",
            /* //motm/125 */ "Parvoviruses",
            /* //motm/126 */ "Epidermal Growth Factor",
            /* //motm/127 */ "Crystallins",
            /* //motm/128 */ "Interferons",
            /* //motm/129 */ "Isocitrate Dehydrogenase",
            /* //motm/130 */ "Riboswitches",
            /* //motm/131 */ "Inteins",
            /* //motm/132 */ "Adenovirus",
            /* //motm/133 */ "Nitric Oxide Synthase",
            /* //motm/134 */ "Integrin",
            /* //motm/135 */ "Integrase",
            /* //motm/136 */ "Nanobodies",
            /* //motm/137 */ "Cytochrome bc1",
            /* //motm/138 */ "Glucansucrase",
            /* //motm/139 */ "DNA Methyltransferases",
            /* //motm/140 */ "Rhomboid Protease GlpG",
            /* //motm/141 */ "O-GlcNAc Transferase",
            /* //motm/142 */ "PDB Pioneers",
            /* //motm/143 */ "Toll-like Receptors",
            /* //motm/144 */ "Complex I",
            /* //motm/145 */ "Messenger RNA Capping",
            /* //motm/146 */ "Aminoglycoside Antibiotics",
            /* //motm/147 */ "Rhodopsin",
            /* //motm/148 */ "Ras Protein",
            /* //motm/149 */ "Leptin",
            /* //motm/150 */ "Sliding Clamps",
            /* //motm/151 */ "Hypoxanthine-guanine phosphoribosyltransferase (HGPRT)",
            /* //motm/152 */ "cAMP-dependent Protein Kinase (PKA)",
            /* //motm/153 */ "Pyruvate Dehydrogenase Complex",
            /* //motm/154 */ "Citric Acid Cycle",
            /* //motm/155 */ "Vitamin D Receptor",
            /* //motm/156 */ "ABO Blood Type Glycosyltransferases",
            /* //motm/157 */ "Transfer-Messenger RNA",
            /* //motm/158 */ "Proton-Gated Urea Channel",
            /* //motm/159 */ "Erythrocruorin",
            /* //motm/160 */ "Actinomycin",
            /* //motm/161 */ "Ricin",
            /* //motm/162 */ "Dermcidin",
            /* //motm/163 */ "HIV Capsid",
            /* //motm/164 */ "Serotonin Receptor",
            /* //motm/165 */ "Designed Protein Cages",
            /* //motm/166 */ "Proteasome",
            /* //motm/167 */ "SNARE Proteins",
            /* //motm/168 */ "DNA Helicase",
            /* //motm/169 */ "HIV Envelope Glycoprotein",
            /* //motm/170 */ "Broadly Neutralizing Antibodies",
            /* //motm/171 */ "Neurotransmitter Transporters",
            /* //motm/172 */ "RecA and Rad51",
            /* //motm/173 */ "Aquaporin",
            /* //motm/174 */ "GFP-like Proteins",
            /* //motm/175 */ "Microtubules",
            /* //motm/176 */ "Dynein",
            /* //motm/177 */ "Apoptosomes",
            /* //motm/178 */ "Ebola Virus Proteins",
            /* //motm/179 */ "Methyl-coenzyme M Reductase",
            /* //motm/180 */ "TAL Effectors",
            /* //motm/181 */ "Cascade and CRISPR",
            /* //motm/182 */ "Insulin Receptor",
            /* //motm/183 */ "Phototropin",
            /* //motm/184 */ "Glucagon",
            /* //motm/185 */ "Titin",
            /* //motm/186 */ "Receptor for Advanced Glycation End Products",
            /* //motm/187 */ "New Delhi Metallo-Beta-Lactamase",
            /* //motm/188 */ "Tetrahydrobiopterin Biosynthesis",
            /* //motm/189 */ "Amyloids",
            /* //motm/190 */ "Two-component Systems",
            /* //motm/191 */ "Glutamate-gated Chloride Receptors",
            /* //motm/192 */ "Vancomycin",
            /* //motm/193 */ "Siderocalin",
            /* //motm/194 */ "Designer Insulins",
            /* //motm/195 */ "RAF Protein Kinases",
            /* //motm/196 */ "Lead Poisoning",
            /* //motm/197 */ "Zika Virus",
            /* //motm/198 */ "Beta-galactosidase",
            /* //motm/199 */ "Monellin",
            /* //motm/200 */ "Quasisymmetry in Icosahedral Viruses",
            /* //motm/201 */ "Isoprene Synthase",
            /* //motm/202 */ "Dipeptidyl Peptidase 4",
            /* //motm/203 */ "Aminopeptidase 1 and Autophagy",
            /* //motm/204 */ "PD-1 (Programmed Cell Death Protein 1)",
            /* //motm/205 */ "Nuclear Pore Complex",
            /* //motm/206 */ "Globin Evolution",
            /* //motm/207 */ "Photoactive Yellow Protein",
            /* //motm/208 */ "Glucose Transporters",
            /* //motm/209 */ "Tissue Transglutaminase and Celiac Disease",
            /* //motm/210 */ "Adenine Riboswitch in Action",
            /* //motm/211 */ "Pilus Machine",
            /* //motm/212 */ "Glutathione Transferases",
            /* //motm/213 */ "Sirtuins",
            /* //motm/214 */ "Chimeric Antigen Receptors",
            /* //motm/215 */ "Aspartate Transcarbamoylase",
            /* //motm/216 */ "Biodegradable Plastic",
            /* //motm/217 */ "Opioid Receptors",
            /* //motm/218 */ "EPSP Synthase and Weedkillers",
            /* //motm/219 */ "Vacuolar ATPase",
            /* //motm/220 */ "Dehalogenases",
            /* //motm/221 */ "Human Papillomavirus and Vaccines",
            /* //motm/222 */ "Proteins and Nanoparticles",
            /* //motm/223 */ "Piezo1 Mechanosensitive Channel",
            /* //motm/224 */ "Legumain",
            /* //motm/225 */ "Phytase",
            /* //motm/226 */ "Aminoglycoside Antibiotics and Resistance",
            /* //motm/227 */ "Telomerase",
            /* //motm/228 */ "Directed Evolution of Enzymes",
            /* //motm/229 */ "Fluorescent RNA Aptamers",
            /* //motm/230 */ "Initiation Factor eIF4E",
            /* //motm/231 */ "Measles Virus Proteins",
            /* //motm/232 */ "Proteins and Biominerals",
            /* //motm/233 */ "S-Nitrosylated Hemoglobin",
            /* //motm/234 */ "MDM2 and Cancer",
            /* //motm/235 */ "AMPA Receptor",
            /* //motm/236 */ "Cyclin and Cyclin-dependent Kinase",
            /* //motm/237 */ "Nanodiscs and HDL",
            /* //motm/238 */ "Ribonucleotide Reductase",
            /* //motm/239 */ "Phospholipase A2",
            /* //motm/240 */ "Hypoxia-Inducible Factors",
            /* //motm/241 */ "Twenty Years of Molecules",
            /* //motm/242 */ "Coronavirus Proteases",
            /* //motm/243 */ "Voltage-gated Sodium Channels",
            /* //motm/244 */ "Photosynthetic Supercomplexes",
            /* //motm/245 */ "Spliceosomes",
            /* //motm/246 */ "SARS-CoV-2 Spike",
            /* //motm/247 */ "Myelin-associated Glycoprotein",
            /* //motm/248 */ "Phytosulfokine Receptor",
            /* //motm/249 */ "SARS-CoV-2 RNA-dependent RNA Polymerase",
            /* //motm/250 */ "Capsaicin Receptor TRPV1",
            /* //motm/251 */ "Adenylyl Cyclase",  // NOV 2020
            /* //motm/252 */ "Hepatitis C Virus Protease/Helicase",  // DEC 2020
            /* //motm/253 */ "Expressome",  // JAN 2021
            /* //motm/254 */ "Cellulose Synthase",  // FEB 2021
            /* //motm/255 */ "Cisplatin and DNA",  // MAR 2021
            /* //motm/256 */ "SARS-CoV-2 Spike and Antibodies",  // APR 2021
            /* //motm/257 */ "Fetal Hemoglobin",  // MAY 2021
            /* //motm/258 */ "TBD"  // JUN 2021
    )

    /**
     * SEARCHING:
     *
     * iterate through the titles and taglines
     * If there is a match with either, collect both lists, merge them,
     * remove dups, and then assemble a MotmEntryInfo list with the index and
     * associated content.
     */
    data class MotmEntryInfo(val theIndexNumber: Int, val tagLine: String, val corpusLine: String)
    fun searchMotmInfo(searchTerm: String): List<MotmEntryInfo> {
        //val startTime = System.currentTimeMillis()
        val s = searchTerm.lowercase(Locale.ROOT)
        val matchTag = (motmTagLines
                .withIndex().filter {it.value.lowercase(Locale.ROOT).contains(s)}
                .map { it.index }).toIntArray()
        val matchCorpus = (corpus
                .withIndex().filter {it.value.lowercase(Locale.ROOT).contains(s)}
                .map { it.index }).toIntArray()

        val mergeBothLists = (intArrayOf(*matchTag, *matchCorpus).sortedByDescending { it }).distinct()

        val resultsList = mergeBothLists.map {
            MotmEntryInfo(it, motmTagLines[it], corpus[it])}

//        Timber.v("TIME LAPSED: %d milliseconds, %d matches",
//                System.currentTimeMillis() - startTime,
//                resultsList.size)

        return resultsList
    }


}
