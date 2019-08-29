/* TestChipsParser.java
 *
 * Copyright (c) 2010, Chris Laforet Software/Christopher Laforet
 * All Rights Reserved
 *
 * Started: Nov 25, 2010
 * Revision Information: $Date$
 *                       $Revision$
 * 
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
 
package com.chrislaforetsoftware.chips.sanitytests;
 
import com.chrislaforetsoftware.chips.message.ChipsMessage;
import com.chrislaforetsoftware.chips.message.ChipsMessageParser;
import com.chrislaforetsoftware.util.Field;
import com.chrislaforetsoftware.util.MessageParseResults;
 
import junit.framework.TestCase;
 
/**
 * @author Christopher Laforet
 *
 */
 
public class TestChipsParser extends TestCase
    {
    static public String GoodChipsMessage = 
            "[031] 01 19920508 0509 1 03 125956\r\n" +
            "SSN: 0045348, ISN: 001673, OSN: 003406\r\n" +
            "[221] 0008 B\r\n" +
            "[260] 000220769618\r\n" +
            "[270] 001663\r\n" +
            "[320] 74300T743013042\r\n" +
            "[321] FX DEAL\r\n" +
            "[412] D 10990765 CITIBANK, LONDON, ENGLAND\r\n" +
            "[422] B UBSWCHZH80A\r\n" +
            "[500] C 005419\r\n" +
            "[507] C 005419 D 000019350043 B BOFSGB2L BANK OF SCOTLAND\r\n" +
            "INTL. DIV., OPERATIONS DEPT.\r\n" +
            "POB 778, BISHOPSGATE EXCHANGE LONDON EC2M 3UB,   ENGLAND";
     
    public void testFieldParser() throws Exception
        {
        MessageParseResults results = ChipsMessageParser.parseMessage(GoodChipsMessage);
        assertTrue(results != null);
        assertTrue(results.fieldCount() != 0);
//System.err.println(results.getErrors());      
        assertTrue(results.errorCount() == 0);
        assertTrue(results.fieldCount() == 10);
         
        Field field = results.getFields().get(0);
        assertEquals(field.getTag(),"031");
        assertEquals(field.getValue(),"01 19920508 0509 1 03 125956\r\nSSN: 0045348, ISN: 001673, OSN: 003406");
        assertEquals(field.toString(),"[031] 01 19920508 0509 1 03 125956\r\nSSN: 0045348, ISN: 001673, OSN: 003406");
 
        field = results.getFields().get(9);
        assertEquals(field.getTag(),"507");
        assertEquals(field.getValue(),"C 005419 D 000019350043 B BOFSGB2L BANK OF SCOTLAND\r\nINTL. DIV., OPERATIONS DEPT.\r\nPOB 778, BISHOPSGATE EXCHANGE LONDON EC2M 3UB,   ENGLAND");
 
        field = results.getFields().get(1);
        assertEquals(field.getTag(),"221");
        assertEquals(field.getValue(),"0008 B");
        }
     
    public void testMessage() throws Exception
        {
        MessageParseResults results = ChipsMessageParser.parseMessage(GoodChipsMessage);
        ChipsMessage message = (ChipsMessage)results.getMessage();
        assertTrue(message != null);
     
        assertTrue(message.doesFieldExist("031"));
        assertTrue(message.doesFieldExist("321"));
        assertTrue(message.doesFieldExist("412"));
        assertTrue(message.doesFieldExist("507"));
        assertFalse(message.doesFieldExist("199"));     
        assertFalse(message.doesFieldExist("324"));
         
        assertEquals(message.getFieldValue("999"),"");
        assertEquals(message.getFieldValue("221"),"0008 B");        
        assertEquals(message.getFieldValue("500"),"C 005419");
        assertEquals(message.getFieldValue("320"),"74300T743013042");
        assertEquals(message.getFieldValue("321"),"FX DEAL");
        assertEquals(message.getFieldValue("507"),"C 005419 D 000019350043 B BOFSGB2L BANK OF SCOTLAND\r\nINTL. DIV., OPERATIONS DEPT.\r\nPOB 778, BISHOPSGATE EXCHANGE LONDON EC2M 3UB,   ENGLAND");
         
        assertEquals(message.toString(),GoodChipsMessage);
         
        assertEquals(message.getAmount(),"000220769618");
        assertEquals(message.getPSN(),"001663");
        assertEquals(message.getSendParticipantReference(),"74300T743013042");
         
        assertEquals(message.getBeneficiaryBank(),"D 10990765 CITIBANK, LONDON, ENGLAND");
        assertEquals(message.getBeneficiary(),"B UBSWCHZH80A");
        assertEquals(message.getOriginator(),"C 005419");
         
        assertEquals(message.getBeneficiaryBankID(),"D 10990765");
        assertEquals(message.getBeneficiaryID(),"B UBSWCHZH80A");
        assertEquals(message.getOriginatorID(),"C 005419");
 
        assertEquals(ChipsMessage.lookupIDCode(message.getBeneficiaryBankID().substring(0,1)),"Demand Deposit Account (DDA)");
        assertEquals(ChipsMessage.lookupIDCode(message.getBeneficiaryID().substring(0,1)),"BIC/SWIFT");
        assertEquals(ChipsMessage.lookupIDCode(message.getOriginatorID().substring(0,1)),"CHIPS Universal ID");
        }
    }