/* TestFedParser.java
 *
 * Copyright (c) 2010, Chris Laforet Software/Christopher Laforet
 * All Rights Reserved
 *
 * Started: Nov 24, 2010
 * Revision Information: $Date$
 *                       $Revision$
  * 
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http: * www.mozilla.org/MPL/
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
 
package com.chrislaforetsoftware.fed.sanitytests;
 
import com.chrislaforetsoftware.fed.message.FedWireIdentifier;
import com.chrislaforetsoftware.fed.message.FedWireMessage;
import com.chrislaforetsoftware.fed.message.FedWireMessageParser;
import com.chrislaforetsoftware.util.Field;
import com.chrislaforetsoftware.util.MessageParseResults;
 
import junit.framework.TestCase;
 
/** Test cases for testing Fed message Parsing.
 * 
 * @author Christopher Laforet
 */
 
public class TestFedParser extends TestCase
    {
    static public String GoodFedMessage = 
        "{1100}02T 3\r\n" +
        "{1110}11221518FT01\r\n" +
        "{1130}W172BEYOND CUTOFF\r\n" +
        "{1500}0222000212T\r\n" +
        "{1510}1000\r\n" +
        "{1520}20101122I1B78A1R000002\r\n" +
        "{2000}000000000100\r\n" +
        "{3100}999999999SOME BANKS INC\r\n" +
        "{3320}2010112200000101\r\n" +
        "{3400}888888888ANOTHER BANKCO NA\r\n" +
        "{3600}CTR\r\n" +
        "{4200}D01230123456789\r\n" +
        "{4320}ABC0099123123123\r\n" +
        "{5000}D000000123456789\r\n" +
        "FANCYTOWN ENERGY COMPANY L.P.\r\n" +
        "200 S RAINER ST STE 1901\r\n" +
        "LOS ANGELES          CA 90071-0192\r\n" +
        "{6000}FANCYTOWN ENERGY";
     
    public void testFieldParser() throws Exception
        {
        MessageParseResults results = FedWireMessageParser.parseMessage(GoodFedMessage);
        assertTrue(results != null);
        assertTrue(results.fieldCount() != 0);
//System.err.println(results.getErrors());      
        assertTrue(results.errorCount() == 0);
        assertTrue(results.fieldCount() == 15);
         
        Field field = results.getFields().get(0);
        assertEquals(field.getTag(),"1100");
        assertEquals(field.getValue(),"02T 3");
        assertEquals(field.toString(),"{1100}02T 3");
         
        field = results.getFields().get(13);
        assertEquals(field.getTag(),"5000");
        assertEquals(field.getValue(),"D000000123456789\r\nFANCYTOWN ENERGY COMPANY L.P.\r\n200 S RAINER ST STE 1901\r\nLOS ANGELES          CA 90071-0192");
        assertEquals(field.toString(),"{5000}D000000123456789\r\nFANCYTOWN ENERGY COMPANY L.P.\r\n200 S RAINER ST STE 1901\r\nLOS ANGELES          CA 90071-0192");
 
        field = results.getFields().get(10);
        assertEquals(field.getTag(),"3600");
        assertEquals(field.getValue(),"CTR");
        }
     
     
    public void testMessage() throws Exception
        {
        MessageParseResults results = FedWireMessageParser.parseMessage(GoodFedMessage);
        FedWireMessage message = (FedWireMessage)results.getMessage();
        assertTrue(message != null);
 
        assertEquals(message.getMessageDisposition(),"02T 3");
        assertEquals(message.getAcceptanceTimeStamp(),"11221518FT01");
        assertEquals(message.getOMAD(),"");
        assertEquals(message.getErrorField(),"W172BEYOND CUTOFF");
         
        assertEquals(message.getSenderSuppliedInformation(),"0222000212T");
        assertEquals(message.getTypeCode(),"10");
        assertEquals(message.getSubTypeCode(),"00");
        assertEquals(message.getTypeAndSubtypeCode(),"1000");
        assertEquals(message.getIMAD(),"20101122I1B78A1R000002");
        assertEquals(message.getAmount(),"000000000100");
        assertEquals(message.getSenderFI(),"999999999SOME BANKS INC");
        assertEquals(message.getSenderABA(),"999999999");
        assertEquals(message.getReceiverFI(),"888888888ANOTHER BANKCO NA");
        assertEquals(message.getReceiverABA(),"888888888");
        assertEquals(message.getBusinessFunctionCode(),"CTR");
         
        assertEquals(message.getFieldValue("9999"),"");
        assertEquals(message.getFieldValue("1520"),message.getIMAD());
        assertEquals(message.getFieldValue("6000"),"FANCYTOWN ENERGY");
         
        assertTrue(message.doesFieldExist("1500"));
        assertTrue(message.doesFieldExist("6000"));
        assertFalse(message.doesFieldExist("1999"));
        assertFalse(message.doesFieldExist(" 1500"));   // note space
         
        assertEquals(message.getOriginatorID(),"D000000123456789");
        assertEquals(message.getOriginator(),"D000000123456789\r\nFANCYTOWN ENERGY COMPANY L.P.\r\n200 S RAINER ST STE 1901\r\nLOS ANGELES          CA 90071-0192");
         
        assertEquals(message.getBeneficiaryID(),"D01230123456789");
         
        FedWireIdentifier identifier = new FedWireIdentifier(message.getBeneficiaryID());
        assertEquals(identifier.getCode(),"D");
        assertEquals(identifier.getIdentifier(),"01230123456789");
        assertEquals(identifier.toString(),"D01230123456789");
        assertEquals(identifier.getCodeDescription(),"Demand Deposit Account (DDA) Number");
         
        identifier = new FedWireIdentifier(message.getOriginatorID());
        assertEquals(identifier.getCode(),"D");
        assertEquals(identifier.getIdentifier(),"000000123456789");
        assertEquals(identifier.toString(),"D000000123456789");
        assertEquals(identifier.getCodeDescription(),"Demand Deposit Account (DDA) Number");
 
        assertEquals(message.toString(),GoodFedMessage);
        }
    }
    