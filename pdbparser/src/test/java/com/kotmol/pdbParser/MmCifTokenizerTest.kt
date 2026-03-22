/*
 *  Copyright 2021 James Andreas
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

package com.kotmol.pdbParser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.InputStreamReader

internal class MmCifTokenizerTest {

    private fun tokenize(text: String): List<MmCifToken> {
        val reader = BufferedReader(InputStreamReader(text.byteInputStream()))
        val tokenizer = MmCifTokenizer(reader)
        val tokens = mutableListOf<MmCifToken>()
        while (true) {
            val t = tokenizer.nextToken()
            if (t == MmCifToken.End) break
            tokens.add(t)
        }
        return tokens
    }

    @Test
    @DisplayName("data_ block token")
    fun testDataBlock() {
        val tokens = tokenize("data_4HHB")
        assertEquals(1, tokens.size)
        assertEquals(MmCifToken.DataBlock("4HHB"), tokens[0])
    }

    @Test
    @DisplayName("loop_ token")
    fun testLoop() {
        val tokens = tokenize("loop_")
        assertEquals(1, tokens.size)
        assertEquals(MmCifToken.Loop, tokens[0])
    }

    @Test
    @DisplayName("tag token with dot")
    fun testTag() {
        val tokens = tokenize("_atom_site.Cartn_x")
        assertEquals(1, tokens.size)
        val tag = tokens[0] as MmCifToken.Tag
        assertEquals("atom_site", tag.category)
        assertEquals("Cartn_x", tag.attribute)
    }

    @Test
    @DisplayName("key-value pair: tag then value")
    fun testKeyValuePair() {
        val tokens = tokenize("_cell.length_a  63.150")
        assertEquals(2, tokens.size)
        assertEquals(MmCifToken.Tag("cell", "length_a"), tokens[0])
        assertEquals(MmCifToken.Value("63.150"), tokens[1])
    }

    @Test
    @DisplayName("plain value '.' (inapplicable)")
    fun testDotValue() {
        val tokens = tokenize(".")
        assertEquals(1, tokens.size)
        assertEquals(MmCifToken.Value("."), tokens[0])
    }

    @Test
    @DisplayName("plain value '?' (missing)")
    fun testQuestionMarkValue() {
        val tokens = tokenize("?")
        assertEquals(1, tokens.size)
        assertEquals(MmCifToken.Value("?"), tokens[0])
    }

    @Test
    @DisplayName("single-quoted string with spaces")
    fun testSingleQuotedValue() {
        val tokens = tokenize("'hello world'")
        assertEquals(1, tokens.size)
        assertEquals(MmCifToken.QuotedValue("hello world"), tokens[0])
    }

    @Test
    @DisplayName("double-quoted string with spaces")
    fun testDoubleQuotedValue() {
        val tokens = tokenize("\"hello world\"")
        assertEquals(1, tokens.size)
        assertEquals(MmCifToken.QuotedValue("hello world"), tokens[0])
    }

    @Test
    @DisplayName("comment line is skipped entirely")
    fun testCommentLine() {
        val tokens = tokenize("# this is a comment\ndata_TEST")
        assertEquals(1, tokens.size)
        assertEquals(MmCifToken.DataBlock("TEST"), tokens[0])
    }

    @Test
    @DisplayName("semicolon multi-line text block")
    fun testSemicolonTextBlock() {
        val input = ";\nline one\nline two\n;"
        val tokens = tokenize(input)
        assertEquals(1, tokens.size)
        val tv = tokens[0] as MmCifToken.TextValue
        assertTrue(tv.text.contains("line one"))
        assertTrue(tv.text.contains("line two"))
    }

    @Test
    @DisplayName("loop_ with 3 tags and 2 rows")
    fun testLoopWithRows() {
        val input = """
            loop_
            _entity.id
            _entity.type
            _entity.desc
            1 polymer 'protein chain'
            2 non-polymer ligand
        """.trimIndent()
        val tokens = tokenize(input)

        // Expected: Loop, Tag, Tag, Tag, Value(1), Value(polymer), QuotedValue(protein chain),
        //           Value(2), Value(non-polymer), Value(ligand)
        assertEquals(MmCifToken.Loop, tokens[0])
        assertEquals(MmCifToken.Tag("entity", "id"),   tokens[1])
        assertEquals(MmCifToken.Tag("entity", "type"), tokens[2])
        assertEquals(MmCifToken.Tag("entity", "desc"), tokens[3])
        assertEquals(MmCifToken.Value("1"),             tokens[4])
        assertEquals(MmCifToken.Value("polymer"),       tokens[5])
        assertEquals(MmCifToken.QuotedValue("protein chain"), tokens[6])
        assertEquals(MmCifToken.Value("2"),             tokens[7])
        assertEquals(MmCifToken.Value("non-polymer"),   tokens[8])
        assertEquals(MmCifToken.Value("ligand"),        tokens[9])
    }

    @Test
    @DisplayName("push-back returns the same token on next call")
    fun testPushBack() {
        val reader = BufferedReader(InputStreamReader("data_X loop_".byteInputStream()))
        val tokenizer = MmCifTokenizer(reader)
        val first = tokenizer.nextToken()
        assertEquals(MmCifToken.DataBlock("X"), first)
        tokenizer.pushBack(first)
        val again = tokenizer.nextToken()
        assertEquals(MmCifToken.DataBlock("X"), again)
        assertEquals(MmCifToken.Loop, tokenizer.nextToken())
    }
}
