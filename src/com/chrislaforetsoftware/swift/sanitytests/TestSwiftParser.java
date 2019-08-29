/* TestSwiftParser.java
 *
 * Copyright (c) 2010, Chris Laforet Software/Christopher Laforet
 * All Rights Reserved
 *
 * Started: Nov 28, 2010
 * Revision Information: $Date: 2010-12-01 12:11:56 $
 *                       $Revision: 1.1 $
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Initial Developer of the Original Code is Chris Laforet from Chris Laforet Software.
 * Portions created by Chris Laforet Software are Copyright (C) 2010.  All Rights Reserved.
 *
 * Contributor(s): Chris Laforet Software.
 */
 
package com.chrislaforetsoftware.swift.sanitytests;
 
import com.chrislaforetsoftware.swift.message.SwiftBlock2Inbound;
import com.chrislaforetsoftware.swift.message.SwiftBlock2Outbound;
import com.chrislaforetsoftware.swift.message.SwiftMessage;
import com.chrislaforetsoftware.swift.message.SwiftMessageParser;
import com.chrislaforetsoftware.util.MessageParseResults;
 
import junit.framework.TestCase;
 
/** JUnit test code for a Swift message parser.
 * 
 * @author Christopher Laforet
 */
 
public class TestSwiftParser extends TestCase
    {
    static private String GoodSwiftMT202Message = "{1:F01ABCDUS10AXXX0000000000}{2:I202MNOPUS40HXYZX}{4:\r\n" +
            ":20:101101012345678A\r\n" +
            ":21:A10112090QQ1-150\r\n" +
            ":32A:101101USD10100,50\r\n" +
            ":52A:ABCDCHAAXXX\r\n" +
            ":58A:QRSTJP90MNO\r\n" +
            ":72:/REC//DEDUCT/\r\n" +
            "-}{5:{MAC:41720873}{CHK:123456789ABC}}";
     
    static private String GoodSwiftMT541Message = "{1:F01ABCDUS20AXXX0000000000}{2:I541MNOPHK50XABCN}{3:{108:101101C9010F99}}{4:\r\n" +
            ":16R:GENL\r\n" +
            ":20C::SEME//101101C9010F99\r\n" +
            ":23G:NEWM\r\n" +
            ":16S:GENL\r\n" +
            ":16R:TRADDET\r\n" +
            ":98A::SETT//20101101\r\n" +
            ":98A::TRAD//20101101\r\n" +
            ":90A::DEAL//PRCT/96,571\r\n" +
            ":35B:ISIN US100001AB10\r\n" +
            "BLAH1 PROPERTY GROUP INC\r\n" +
            ":16S:TRADDET\r\n" +
            ":16R:FIAC\r\n" +
            ":36B::SETT//FAMT/12345,\r\n" +
            ":97A::SAFE//FUN03\r\n" +
            ":16S:FIAC\r\n" +
            ":16R:SETDET\r\n" +
            ":22F::SETR//TRAD\r\n" +
            ":16R:SETPRTY\r\n" +
            ":95P::PSET//ABCDGBAA\r\n" +
            ":16S:SETPRTY\r\n" +
            ":16R:SETPRTY\r\n" +
            ":95R::SELL/ECLR/11996\r\n" +
            ":16S:SETPRTY\r\n" +
            ":16R:SETPRTY\r\n" +
            ":95R::DEAG/ECLR/11996\r\n" +
            ":16S:SETPRTY\r\n" +
            ":16R:AMT\r\n" +
            ":19A::SETT//USD12345,98\r\n" +
            ":16S:AMT\r\n" +
            ":16S:SETDET\r\n" +
            "-}";
     
    static private String GoodSwiftMT191Message = "{1:F01MNOPUS10AXXX1684207429}{2:O1910953101130WXYZHK10AXXX36030001231011300045N}{4:\r\n" +
            ":20:123456780123\r\n" +
            ":21:2010113000000199\r\n" +
            ":32B:USD45,\r\n" +
            ":57A:ABCDUS20XXX\r\n" +
            ":71B:/COMM/\r\n" +
            "-}{5:{CHK:880A550C6F00}{MAC:00000000}}"; 
     
     
    public void testMT202() throws Exception
        {
        MessageParseResults results = SwiftMessageParser.parseMessage(GoodSwiftMT202Message);
        assertNotNull(results.getMessage());
         
        SwiftMessage message = (SwiftMessage)results.getMessage();
        assertEquals(message.getBlock1().getBIC12(),"ABCDUS10AXXX");
        assertEquals(message.getBlock1().getBIC11(),"ABCDUS10XXX");
        assertEquals(message.getBlock1().getBIC8(),"ABCDUS10");
 
        SwiftBlock2Inbound block2 = message.getBlock2Inbound();
        assertNotNull(block2);
        assertNull(message.getBlock2Outbound());
         
        assertEquals(message.getMT(),"202");
         
        assertEquals(block2.getBIC12(),"MNOPUS40HXYZ");
        assertEquals(block2.getBIC11(),"MNOPUS40XYZ");
        assertEquals(block2.getBIC8(),"MNOPUS40");
         
        assertEquals(message.getBlock4().getFields().size(),6);
         
        assertEquals(GoodSwiftMT202Message,message.toString());
 
        assertEquals(message.getBlock4().getField("20"),"101101012345678A");
        assertEquals(message.getBlock4().getField("21"),"A10112090QQ1-150");
        assertEquals(message.getBlock4().getField("58A"),"QRSTJP90MNO");
        }
 
    public void testMT541() throws Exception
        {
        MessageParseResults results = SwiftMessageParser.parseMessage(GoodSwiftMT541Message);
        assertNotNull(results.getMessage());
         
        SwiftMessage message = (SwiftMessage)results.getMessage();
        assertEquals(message.getBlock1().getBIC12(),"ABCDUS20AXXX");
        assertEquals(message.getBlock1().getBIC11(),"ABCDUS20XXX");
        assertEquals(message.getBlock1().getBIC8(),"ABCDUS20");
     
        SwiftBlock2Inbound block2 = message.getBlock2Inbound();
        assertNotNull(block2);
        assertNull(message.getBlock2Outbound());
         
        assertEquals(message.getMT(),"541");
         
        assertEquals(block2.getBIC12(),"MNOPHK50XABC");
        assertEquals(block2.getBIC11(),"MNOPHK50ABC");
        assertEquals(block2.getBIC8(),"MNOPHK50");
//----------------      
        assertEquals(message.getBlock4().getFields().size(),29);
         
        assertEquals(GoodSwiftMT541Message,message.toString());
     
        assertEquals(message.getBlock4().getField("20C"),":SEME//101101C9010F99");
        assertEquals(message.getBlock4().getField("23G"),"NEWM");
        String [] fields = message.getBlock4().getFields("16R");
        assertEquals(fields.length,8);
        assertEquals(fields[0],"GENL");
        assertEquals(fields[1],"TRADDET");
        assertEquals(fields[5],"SETPRTY");
        assertEquals(fields[7],"AMT");
        }
 
    public void testMT191() throws Exception
        {
        MessageParseResults results = SwiftMessageParser.parseMessage(GoodSwiftMT191Message);
        assertNotNull(results.getMessage());
 
        SwiftMessage message = (SwiftMessage)results.getMessage();
        assertEquals(message.getBlock1().getBIC12(),"MNOPUS10AXXX");
        assertEquals(message.getBlock1().getBIC11(),"MNOPUS10XXX");
        assertEquals(message.getBlock1().getBIC8(),"MNOPUS10");
         
        SwiftBlock2Outbound block2 = message.getBlock2Outbound();
        assertNotNull(block2);
        assertNull(message.getBlock2Inbound());
         
        assertEquals(message.getMT(),"191");
         
        assertEquals(block2.getBIC12(),"WXYZHK10AXXX");
        assertEquals(block2.getBIC11(),"WXYZHK10XXX");
        assertEquals(block2.getBIC8(),"WXYZHK10");
         
        assertEquals(block2.getSequence(),"000123");
        assertEquals(block2.getOutputDate(),"101130");
        assertEquals(block2.getInputTime(),"0953");
        assertEquals(block2.getSessionNo(),"3603");
 
        assertEquals(message.getBlock4().getFields().size(),5);
         
        assertEquals(GoodSwiftMT191Message,message.toString());
         
        assertEquals(message.getBlock4().getField("20"),"123456780123");
        assertEquals(message.getBlock4().getField("21"),"2010113000000199");
        assertEquals(message.getBlock4().getField("32B"),"USD45,");
        }
    }