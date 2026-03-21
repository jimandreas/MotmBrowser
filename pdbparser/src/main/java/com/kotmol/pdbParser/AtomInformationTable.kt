/*
 *  Copyright 2020 James Andreas
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
        "ArrayInDataClass"
)
package com.kotmol.pdbParser

object AtomInformationTable {

    // generated from JimConvertAtomInfoTable.kt (IntelliJ Idea)
    data class AtomNameNumber(
            var number: Int = 0,
            var symbol: String = "",
            var name: String = "",
            var vanDerWaalsRadius: Int = 300, // default size if not known
            var color: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
    )

    val atomSymboltoAtomNumNameColor = HashMap<String, AtomNameNumber>()

    val atomTable = listOf(
            AtomNameNumber(number = 1, symbol = "H", name = "hydrogen", vanDerWaalsRadius = 120, color = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)),
            AtomNameNumber(number = 2, symbol = "HE", name = "helium", vanDerWaalsRadius = 140, color = floatArrayOf(0.8509804f, 1.0f, 1.0f, 1.0f)),
            AtomNameNumber(number = 3, symbol = "LI", name = "lithium", vanDerWaalsRadius = 182, color = floatArrayOf(0.69803923f, 0.12941177f, 0.12941177f, 1.0f)),
            AtomNameNumber(number = 4, symbol = "BE", name = "beryllium", vanDerWaalsRadius = 153, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 5, symbol = "B", name = "boron", vanDerWaalsRadius = 192, color = floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f)),
            AtomNameNumber(number = 6, symbol = "C", name = "carbon", vanDerWaalsRadius = 170, color = floatArrayOf(0.8901961f, 0.7647059f, 0.59607846f, 1.0f)),
            AtomNameNumber(number = 7, symbol = "N", name = "nitrogen", vanDerWaalsRadius = 155, color = floatArrayOf(0.5294118f, 0.80784315f, 0.9019608f, 1.0f)),
            AtomNameNumber(number = 8, symbol = "O", name = "oxygen", vanDerWaalsRadius = 152, color = floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f)),
            AtomNameNumber(number = 9, symbol = "F", name = "fluorine", vanDerWaalsRadius = 147, color = floatArrayOf(0.85490197f, 0.64705884f, 0.1254902f, 1.0f)),
            AtomNameNumber(number = 10, symbol = "NE", name = "neon", vanDerWaalsRadius = 154, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 11, symbol = "NA", name = "sodium", vanDerWaalsRadius = 227, color = floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f)),
            AtomNameNumber(number = 12, symbol = "MG", name = "magnesium", vanDerWaalsRadius = 173, color = floatArrayOf(0.13333334f, 0.54509807f, 0.13333334f, 1.0f)),
            AtomNameNumber(number = 13, symbol = "AL", name = "aluminium", vanDerWaalsRadius = 184, color = floatArrayOf(0.4117647f, 0.4117647f, 0.4117647f, 1.0f)),
            AtomNameNumber(number = 14, symbol = "SI", name = "silicon", vanDerWaalsRadius = 210, color = floatArrayOf(0.85490197f, 0.64705884f, 0.1254902f, 1.0f)),
            AtomNameNumber(number = 15, symbol = "P", name = "phosphorus", vanDerWaalsRadius = 180, color = floatArrayOf(1.0f, 0.6666667f, 0.0f, 1.0f)),
            AtomNameNumber(number = 16, symbol = "S", name = "sulfur", vanDerWaalsRadius = 180, color = floatArrayOf(1.0f, 1.0f, 0.0f, 1.0f)),
            AtomNameNumber(number = 17, symbol = "CL", name = "chlorine", vanDerWaalsRadius = 175, color = floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f)),
            AtomNameNumber(number = 18, symbol = "AR", name = "argon", vanDerWaalsRadius = 188, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 19, symbol = "K", name = "potassium", vanDerWaalsRadius = 275, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 20, symbol = "CA", name = "calcium", vanDerWaalsRadius = 231, color = floatArrayOf(0.4117647f, 0.4117647f, 0.4117647f, 1.0f)),
            AtomNameNumber(number = 21, symbol = "SC", name = "scandium", vanDerWaalsRadius = 211, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 22, symbol = "TI", name = "titanium", vanDerWaalsRadius = 300, color = floatArrayOf(0.4117647f, 0.4117647f, 0.4117647f, 1.0f)),
            AtomNameNumber(number = 23, symbol = "V", name = "vanadium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 24, symbol = "CR", name = "chromium", vanDerWaalsRadius = 300, color = floatArrayOf(0.4117647f, 0.4117647f, 0.4117647f, 1.0f)),
            AtomNameNumber(number = 25, symbol = "MN", name = "manganese", vanDerWaalsRadius = 300, color = floatArrayOf(0.4117647f, 0.4117647f, 0.4117647f, 1.0f)),
            AtomNameNumber(number = 26, symbol = "FE", name = "iron", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.6666667f, 0.0f, 1.0f)),
            AtomNameNumber(number = 27, symbol = "CO", name = "cobalt", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 28, symbol = "NI", name = "nickel", vanDerWaalsRadius = 163, color = floatArrayOf(0.5019608f, 0.15686275f, 0.15686275f, 1.0f)),
            AtomNameNumber(number = 29, symbol = "CU", name = "copper", vanDerWaalsRadius = 140, color = floatArrayOf(0.5019608f, 0.15686275f, 0.15686275f, 1.0f)),
            AtomNameNumber(number = 30, symbol = "ZN", name = "zinc", vanDerWaalsRadius = 139, color = floatArrayOf(0.5019608f, 0.15686275f, 0.15686275f, 1.0f)),
            AtomNameNumber(number = 31, symbol = "GA", name = "gallium", vanDerWaalsRadius = 187, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 32, symbol = "GE", name = "germanium", vanDerWaalsRadius = 211, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 33, symbol = "AS", name = "arsenic", vanDerWaalsRadius = 185, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 34, symbol = "SE", name = "selenium", vanDerWaalsRadius = 190, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 35, symbol = "BR", name = "bromine", vanDerWaalsRadius = 185, color = floatArrayOf(0.5019608f, 0.15686275f, 0.15686275f, 1.0f)),
            AtomNameNumber(number = 36, symbol = "KR", name = "krypton", vanDerWaalsRadius = 202, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 37, symbol = "RB", name = "rubidium", vanDerWaalsRadius = 303, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 38, symbol = "SR", name = "strontium", vanDerWaalsRadius = 249, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 39, symbol = "Y", name = "yttrium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 40, symbol = "ZR", name = "zirconium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 41, symbol = "NB", name = "niobium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 42, symbol = "MO", name = "molybdenum", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 43, symbol = "TC", name = "technetium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 44, symbol = "RU", name = "ruthenium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 45, symbol = "RH", name = "rhodium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 46, symbol = "PD", name = "palladium", vanDerWaalsRadius = 163, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 47, symbol = "AG", name = "silver", vanDerWaalsRadius = 172, color = floatArrayOf(0.4117647f, 0.4117647f, 0.4117647f, 1.0f)),
            AtomNameNumber(number = 48, symbol = "CD", name = "cadmium", vanDerWaalsRadius = 158, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 49, symbol = "IN", name = "indium", vanDerWaalsRadius = 193, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 50, symbol = "SN", name = "tin", vanDerWaalsRadius = 217, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 51, symbol = "SB", name = "antimony", vanDerWaalsRadius = 206, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 52, symbol = "TE", name = "tellurium", vanDerWaalsRadius = 206, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 53, symbol = "I", name = "iodine", vanDerWaalsRadius = 198, color = floatArrayOf(0.627451f, 0.1254902f, 0.9411765f, 1.0f)),
            AtomNameNumber(number = 54, symbol = "XE", name = "xenon", vanDerWaalsRadius = 216, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 55, symbol = "CS", name = "caesium", vanDerWaalsRadius = 343, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 56, symbol = "BA", name = "barium", vanDerWaalsRadius = 268, color = floatArrayOf(1.0f, 0.6666667f, 0.0f, 1.0f)),
            AtomNameNumber(number = 57, symbol = "LA", name = "lanthanum", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 58, symbol = "CE", name = "cerium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 59, symbol = "PR", name = "praseodymium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 60, symbol = "ND", name = "neodymium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 61, symbol = "PM", name = "promethium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 62, symbol = "SM", name = "samarium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 63, symbol = "EU", name = "europium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 64, symbol = "GD", name = "gadolinium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 65, symbol = "TB", name = "terbium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 66, symbol = "DY", name = "dysprosium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 67, symbol = "HO", name = "holmium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 68, symbol = "ER", name = "erbium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 69, symbol = "TM", name = "thulium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 70, symbol = "YB", name = "ytterbium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 71, symbol = "LU", name = "lutetium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 72, symbol = "HF", name = "hafnium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 73, symbol = "TA", name = "tantalum", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 74, symbol = "W", name = "tungsten", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 75, symbol = "RE", name = "rhenium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 76, symbol = "OS", name = "osmium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 77, symbol = "IR", name = "iridium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 78, symbol = "PT", name = "platinum", vanDerWaalsRadius = 175, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 79, symbol = "AU", name = "gold", vanDerWaalsRadius = 166, color = floatArrayOf(0.85490197f, 0.64705884f, 0.1254902f, 1.0f)),
            AtomNameNumber(number = 80, symbol = "HG", name = "mercury", vanDerWaalsRadius = 155, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 81, symbol = "TL", name = "thallium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 82, symbol = "PB", name = "lead", vanDerWaalsRadius = 202, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 83, symbol = "BI", name = "bismuth", vanDerWaalsRadius = 207, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 84, symbol = "PO", name = "polonium", vanDerWaalsRadius = 197, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 85, symbol = "AT", name = "astatine", vanDerWaalsRadius = 202, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 86, symbol = "RN", name = "radon", vanDerWaalsRadius = 220, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 87, symbol = "FR", name = "francium", vanDerWaalsRadius = 348, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 88, symbol = "RA", name = "radium", vanDerWaalsRadius = 283, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 89, symbol = "AC", name = "actinium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 90, symbol = "TH", name = "thorium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 91, symbol = "PA", name = "protactinium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 92, symbol = "U", name = "uranium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 93, symbol = "NP", name = "neptunium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 94, symbol = "PU", name = "plutonium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 95, symbol = "AM", name = "americium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 96, symbol = "CM", name = "curium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 97, symbol = "BK", name = "berkelium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 98, symbol = "CF", name = "californium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 99, symbol = "ES", name = "einsteinium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 100, symbol = "FM", name = "fermium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 101, symbol = "MD", name = "mendelevium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 102, symbol = "NO", name = "nobelium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 103, symbol = "LR", name = "lawrencium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 104, symbol = "RF", name = "rutherfordium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 105, symbol = "DB", name = "dubnium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 106, symbol = "SG", name = "seaborgium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 107, symbol = "BH", name = "bohrium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 108, symbol = "HS", name = "hassium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 109, symbol = "MT", name = "meitnerium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 110, symbol = "DS", name = "darmstadtium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 111, symbol = "RG", name = "roentgenium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 112, symbol = "CN", name = "copernicium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 113, symbol = "UUT", name = "ununtrium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 114, symbol = "FL", name = "flerovium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 115, symbol = "UUP", name = "ununpentium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 116, symbol = "LV", name = "livermorium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 117, symbol = "UUS", name = "ununseptium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f)),
            AtomNameNumber(number = 118, symbol = "UUO", name = "ununoctium", vanDerWaalsRadius = 300, color = floatArrayOf(1.0f, 0.078431375f, 0.5764706f, 1.0f))
    )

    init {
        val iter = atomTable.iterator()
        while (iter.hasNext()) {
            val thisAtom = iter.next()
            atomSymboltoAtomNumNameColor[thisAtom.symbol] = thisAtom
        }
    }
}
