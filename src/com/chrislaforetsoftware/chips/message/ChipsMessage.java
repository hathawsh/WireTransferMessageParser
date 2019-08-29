/* ChipsMessage.java
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
 
package com.chrislaforetsoftware.chips.message;
 
import java.util.HashMap;
import java.util.List;
 
import com.chrislaforetsoftware.util.CodeLookup;
import com.chrislaforetsoftware.util.Field;
 
/** Contains a CHIPS message.  CHIPS is the CLEARING HOUSE PAYMENTS COMPANY L.L.C.
 * 
 * @author Christopher Laforet
 */
 
public class ChipsMessage
    {
    // CHIPS Message Types
    public static String ChipsPaymentRequestMessage = "10";
    public static String ChipsPaymentStoredResponseMessage = "25";
    public static String ChipsPaymentPreferenceMessage = "02";
    public static String ChipsPaymentResolverNotificationMessage = "38";
    public static String ChipsPaymentDeleteMessage = "01";
 
    public static String ChipsGeneralServiceMessage = "22";
    public static String ChipsServiceResponseMessage = "27";
    public static String ChipsServiceNotificationMessage = "36";
     
    // mandatory fields
    private String _amount;
    private String _psn;
    private String _sendParticipantReference;
 
    // lookup for identifier type
    private static CodeLookup [] _idCode = 
        {
        new CodeLookup("C","CHIPS Universal ID"),
        new CodeLookup("D","Demand Deposit Account (DDA)"),
        new CodeLookup("B","BIC/SWIFT"),
        new CodeLookup("F","Financial Telecommunications"),
        new CodeLookup("1","Non-Bank Identifier"),
        new CodeLookup("2","Non-Bank Identifier"),
        new CodeLookup("3","Non-Bank Identifier"),
        new CodeLookup("4","Non-Bank Identifier"),
        new CodeLookup("5","Non-Bank Identifier"),
        new CodeLookup("9","Non-Bank Identifier")
        };
 
    // lookup for additional payment data (APD) from field 820
    @SuppressWarnings("unused")
    private static CodeLookup [] _apdCode = 
        {
        new CodeLookup("01","UN-EDIFACT"),
        new CodeLookup("02","ANSI X12"),
        new CodeLookup("03","SWIFT"),
        new CodeLookup("04","IXML (ISO 20022)"),
        new CodeLookup("05","GXML (General XML)"),
        new CodeLookup("06","S820 (STP 820)"),
        new CodeLookup("07","Related Remittance Information (field 825) required"),
        new CodeLookup("08","RMTS (Structured remittance)"),
        new CodeLookup("09","PROP (Proprietary code)")
        };
 
    // additional fields
    private List<Field> _fields;
    private HashMap<String,Field> _fieldMap = new HashMap<String,Field>();
 
    /** Attempts to create a FedWireMessage object from a collection
     * of fields.
     * 
     * @param Fields the fields contained in the message.
     */
    public ChipsMessage(List<Field> Fields,List<String> Errors)
        {
        _fields = Fields;
         
        for (Field field : Fields)
            {
            if (_fieldMap.containsKey(field.getTag()))
                Errors.add("Duplicate field: " + field.getTag() + " exists more than once.");
            else
                _fieldMap.put(field.getTag(),field);
            }
 
        // mandatory fields
        Field match = _fieldMap.get("260");
        if (match != null)
            _amount = match.getValue();
        else
            Errors.add("Missing mandatory field: Field 260.");
 
        match = _fieldMap.get("270");
        if (match != null)
            _psn = match.getValue();
        else
            Errors.add("Missing mandatory field: Field 270.");
 
        match = _fieldMap.get("320");
        if (match != null)
            _sendParticipantReference = match.getValue();
        else
            Errors.add("Missing mandatory field: Field 320.");
        }
 
     
    /** Retrieves field 260, the amount field..
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getAmount()
        {
        return _amount == null ? "" : _amount;
        }
 
     
    /** Retrieves field 270, the payment sequence number (PSN) field..
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getPSN()
        {
        return _psn == null ? "" : _psn;
        }
 
     
    /** Retrieves field 320, the send participant reference field..
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getSendParticipantReference()
        {
        return _sendParticipantReference == null ? "" : _sendParticipantReference;
        }
 
     
    /** Attempts to return field 031, if it exists.  This is header information.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getHeader()
        {
        return getFieldValue("031");
        }
 
     
    /** Attempts to return field 201, if it exists.  This is the
     * Identification Tag formed of the Format version, value date, 
     * and send participant number.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getIdentificationTag()
        {
        return getFieldValue("201");
        }
 
     
    /** Attempts to return field 211, if it exists.  This is the
     * Disposition Tag formed of the receive participant, the
     * beneficiary type, compression flag, and the preference flag.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getDispositionTag()
        {
        return getFieldValue("211");
        }   
 
     
    /** Attempts to return field 221, if it exists.  This is the
     * Delivery Tag formed of the receive participant and the beneficiary type.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getDeliveryTag()
        {
        return getFieldValue("221");
        }   
 
     
    /** Attempts to return field 301, if it exists.  This is the
     * Charges  Information which contains details of charges if the
     * beneficiary is N.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getChargesInformation()
        {
        return getFieldValue("301");
        }
 
     
    /** Attempts to return field 321, if it exists.  This is the
     * Related Bank Reference Number.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getRelatedBankReference()
        {
        return getFieldValue("321");
        }   
 
     
    /** Attempts to return field 400/401/402, if one exists.  This is the
     * Intermediary Bank Information (IBK).
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getIntermediaryBank()
        {
        String value = getFieldValue("400");
        if (value.length() > 0)
            return value;
        value = getFieldValue("401");
        if (value.length() > 0)
            return value;
        return getFieldValue("402");
        }
     
     
    /** Attempts to return the ID-code and identifier from field 400/401/402,
     * the Intermediary Financial Institution, if it exists.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getIntermediaryBankID()
        {
        String fi = getIntermediaryBank();
        if (fi.length() == 0)
            return "";
        return decodeID(fi);
        }
     
     
    /** Attempts to retrieve the ID code and number from
     * the field (code-letter+space+ID).
     * 
     * @param Value the value string to parse.
     * @return a string containing the ID or an empty string if not found.
     */
    private String decodeID(String Value)
        {
        if (Value.length() <= 2)
            return "";
        if (Value.charAt(1) != ' ')
            return "";
        int index = Value.indexOf(' ',2);
        if (index > 0)
            return Value.substring(0,index);
        return Value;
        }
 
     
    /** Attempts to return field 410/411/412, if one exists.  This is the
     * Beneficiary Bank Information (BBK).
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getBeneficiaryBank()
        {
        String value = getFieldValue("410");
        if (value.length() > 0)
            return value;
        value = getFieldValue("411");
        if (value.length() > 0)
            return value;
        return getFieldValue("412");
        }
     
     
    /** Attempts to return the ID-code and identifier from field 410/411/412,
     * the Intermediary Financial Institution, if it exists.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getBeneficiaryBankID()
        {
        String fi = getBeneficiaryBank();
        if (fi.length() == 0)
            return "";
        return decodeID(fi);
        }
     
 
    /** Attempts to return field 420/421/422, if one exists.  This is the
     * Beneficiary (BNF).
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getBeneficiary()
        {
        String value = getFieldValue("420");
        if (value.length() > 0)
            return value;
        value = getFieldValue("421");
        if (value.length() > 0)
            return value;
        return getFieldValue("422");
        }   
     
     
    /** Attempts to return the ID-code and identifier from field 420/421/422,
     * the Beneficiary, if it exists.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getBeneficiaryID()
        {
        String fi = getBeneficiary();
        if (fi.length() == 0)
            return "";
        return decodeID(fi);
        }
 
     
    /** Attempts to return field 500/501/502, if one exists.  This is the
     * Originator Information (ORG).
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getOriginator()
        {
        String value = getFieldValue("500");
        if (value.length() > 0)
            return value;
        value = getFieldValue("501");
        if (value.length() > 0)
            return value;
        return getFieldValue("502");
        }   
     
     
    /** Attempts to return the ID-code and identifier from field 500/501/502,
     * the Originator, if it exists.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getOriginatorID()
        {
        String fi = getOriginator();
        if (fi.length() == 0)
            return "";
        return decodeID(fi);
        }
 
     
    /** Attempts to return field 510/511/512, if one exists.  This is the
     * Originator's Bank (OGB).
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getOriginatorBank()
        {
        String value = getFieldValue("510");
        if (value.length() > 0)
            return value;
        value = getFieldValue("511");
        if (value.length() > 0)
            return value;
        return getFieldValue("512");
        }
     
     
    /** Attempts to return the ID-code and identifier from field 510/511/512,
     * the Originator's Bank, if it exists.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getOriginatorBankID()
        {
        String fi = getOriginatorBank();
        if (fi.length() == 0)
            return "";
        return decodeID(fi);
        }
     
 
    /** Attempts to return field 520/521/522, if one exists.  This is the
     * Instructing Bank (INS).
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getInstructingBank()
        {
        String value = getFieldValue("520");
        if (value.length() > 0)
            return value;
        value = getFieldValue("521");
        if (value.length() > 0)
            return value;
        return getFieldValue("522");
        }   
     
     
    /** Attempts to return the ID-code and identifier from field 520/521/522,
     * the Instructing Bank, if it exists.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getInstructingBankID()
        {
        String fi = getInstructingBank();
        if (fi.length() == 0)
            return "";
        return decodeID(fi);
        }
     
     
    /** Determines if the field exists in the message.
     * 
     * @param FieldTag the tag to look for.
     * @return true if there is a field tagged appropriately.
     */
    public boolean doesFieldExist(String FieldTag)
        {
        return _fieldMap.containsKey(FieldTag);
        }
     
     
    /** Attempts to return the value attached to a field tag
     * if it is found in the list of fields.
     * 
     * @param FieldTag the tag (e.g. 1500) to find.
     * @return the value if found or an empty string if field does not exist.
     */
    public String getFieldValue(String FieldTag)
        {
        if (_fieldMap.containsKey(FieldTag))
            return _fieldMap.get(FieldTag).getValue();
        return "";
        }
     
     
    /** Returns the full message formatted with CRLF between each field.
     * 
     * @return the message.
     */
    @Override
    public String toString()
        {
        StringBuilder sb  = new StringBuilder(2048);
        for (Field field : _fields)
            {
            if (sb.length() > 0)
                sb.append("\r\n");
            sb.append(field.toString());
            }
        return sb.toString();
        }
     
     
    // ---------------------------------------
    // Helpful utility methods
    // ---------------------------------------
 
    /** Decodes an ID code and returns its descriptive text.
     * 
     * @param Code the code to look up.
     * @return the description if found or an empty string if not found.
     */
    public static String lookupIDCode(String Code)
        {
        for (CodeLookup lookup : _idCode)
            {
            if (lookup.getCode().compareTo(Code) == 0)
                return lookup.getDescription();
            }
        return "";
        }
    }