/* Message.java
 *
 * Copyright (c) 2010, Chris Laforet Software/Christopher Laforet
 * All Rights Reserved
 *
 * Started: Nov 23, 2010
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
 
package com.chrislaforetsoftware.fed.message;
 
import java.util.HashMap;
import java.util.List;
 
import com.chrislaforetsoftware.util.CodeLookup;
import com.chrislaforetsoftware.util.Field;
 
/** Contains a FedWire message.
 * 
 * @author Christopher Laforet
 */
 
public class FedWireMessage
    {
    // TYPE and SUBTYPE codes for field {1510}
    public static String TYPECODE_FUNDS_TRANSFER = "10";
    public static String TYPECODE_FOREIGN_TRANSFER = "15";
    public static String TYPECODE_SETTLEMENT_TRANSFER = "16";
     
    public static String SUBTYPECODE_BASIC_FUNDS_TRANSFER = "00";
    public static String SUBTYPECODE_REQUEST_FOR_REVERSAL = "01";
    public static String SUBTYPECODE_REVERSAL_OF_TRANSFER = "02";
    public static String SUBTYPECODE_REQUEST_REVERSAL_OF_PRIOR_DAY_TRANSFER = "07";
    public static String SUBTYPECODE_REVERSAL_OF_PRIOR_DAY_TRANSFER = "08";
    public static String SUBTYPECODE_AS_OF_ADJUSTMENT = "20";
    public static String SUBTYPECODE_REQUEST_FOR_CREDIT = "31";
    public static String SUBTYPECODE_FUNDS_TRANSFER_HONORING_REQUEST_FOR_CREDIT = "32";
    public static String SUBTYPECODE_REFUSAL_TO_HONOR_REQUEST_FOR_CREDIT = "33";
    public static String SUBTYPECODE_SERVICE_MESSAGE = "90";
 
    // lookup for advice fields {6210}, {6310}, {6410}, 
    private static CodeLookup [] _adviceCode = 
                {
                new CodeLookup("LTR","Letter"),
                new CodeLookup("PHN","Telephone"),
                new CodeLookup("TLX","Telex"),
                new CodeLookup("WRE","Wire"),
                new CodeLookup("HLD","Hold"),
                };
     
    private static CodeLookup [] _idCode = 
                {
                new CodeLookup("1","Passport Number"),
                new CodeLookup("2","Tax Identification Number"),
                new CodeLookup("3","Driver License Number"),
                new CodeLookup("4","Alien Registration Number"),
                new CodeLookup("5","Corporate Identification"),
                new CodeLookup("9","Other Identification"),
                new CodeLookup("B","S.W.I.F.T. Bank Identifier Code (BIC)"),
                new CodeLookup("C","CHIPS Participant"),
                new CodeLookup("D","Demand Deposit Account (DDA) Number"),
                new CodeLookup("F","Fed Routing Number"),
                new CodeLookup("T","S.W.I.F.T. BIC or Bank Entity Identifier (BEI) and account number"),
                };
     
     
    // lookup for field {3600}
    private static CodeLookup [] _businessFunction = 
                {
                new CodeLookup("BTR","Bank Transfer (Beneficiary is a bank)"),
                new CodeLookup("FFR","Fed Funds Returned"),
                new CodeLookup("CTR","Customer Transfer (Beneficiary is a not a bank)"),
                new CodeLookup("CTRCOV","Cover Payment"),
                new CodeLookup("FFS","Fed Funds Sold"),
                new CodeLookup("DEP","Deposit to Sender’s Account"),
                new CodeLookup("DRB","Bank-to-Bank Drawdown Request"),
                new CodeLookup("CKS","Check Same Day Settlement"),
                new CodeLookup("DRC","Customer or Corporate Drawdown Request"),
                new CodeLookup("SVC","Service Message"),
                new CodeLookup("DRW","Drawdown Payment"),
                };
     
    // mandatory fields
    private String _senderSupplied;     // {1500}
    private String _typeCode;           // {1510}
    private String _subtypeCode;
    private String _imad;               // {1520} 
    private String _amount;             // {2000}
    private String _senderFI;           // {3100}
    private String _receiverFI;         // {3400}
    private String _businessFuncCode;   // {3600}
     
    // fedwire-appended fields
    private String _messageDisposition;     // {1100}
    private String _acceptanceTimestamp;    // {1110}
    private String _omad;                   // {1120}
    private String _error;                  // {1130}
     
    // additional fields
    private List<Field> _fields;
    private HashMap<String,Field> _fieldMap = new HashMap<String,Field>();
 
    /** Attempts to create a FedWireMessage object from a collection
     * of fields.
     * 
     * @param Fields the fields contained in the message.
     */
    public FedWireMessage(List<Field> Fields,List<String> Errors)
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
        Field match = _fieldMap.get("1500");
        if (match != null)
            _senderSupplied = match.getValue();
        else
            Errors.add("Missing mandatory field: Field 1500.");
         
        match = _fieldMap.get("1510");
        if (match != null)
            {
            String value = match.getValue();
            if (value.length() == 4)
                {
                _typeCode = value.substring(0,2);
                _subtypeCode = value.substring(2);
                }
            else
                Errors.add("Missing Type/Subtype Code: Field 1510");
            }
        else
            Errors.add("Missing mandatory field: Field 1510.");
         
        match = _fieldMap.get("1520");
        if (match != null)
            _imad = match.getValue();
        else
            Errors.add("Missing mandatory field: Field 1520.");
         
        match = _fieldMap.get("2000");
        if (match != null)
            _amount = match.getValue();
        else
            Errors.add("Missing mandatory field: Field 2000.");
         
        match = _fieldMap.get("3100");
        if (match != null)
            _senderFI = match.getValue();
        else
            Errors.add("Missing mandatory field: Field 3100.");
         
        match = _fieldMap.get("3400");
        if (match != null)
            _receiverFI = match.getValue();
        else
            Errors.add("Missing mandatory field: Field 3400.");
         
        match = _fieldMap.get("3600");
        if (match != null)
            _businessFuncCode = match.getValue();
        else
            Errors.add("Missing mandatory field: Field 3600.");
         
        // fedwire-appended fields
        match = _fieldMap.get("1100");
        if (match != null)
            _messageDisposition = match.getValue();
         
        match = _fieldMap.get("1110");
        if (match != null)
            _acceptanceTimestamp = match.getValue();
         
        match = _fieldMap.get("1120");
        if (match != null)
            _omad = match.getValue();
         
        match = _fieldMap.get("1130");
        if (match != null)
            _error = match.getValue();
        }
     
     
    /** Retrieves field 1500, the sender supplied information.
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getSenderSuppliedInformation()
        {
        return _senderSupplied == null ? "" : _senderSupplied;
        }
     
     
    /** Retrieves field 1510, the two digit code.
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getTypeCode()
        {
        return _typeCode == null ? "" : _typeCode;
        }
     
     
    /** Retrieves field 1520, the two digit subtype code.
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getSubTypeCode()
        {
        return _subtypeCode == null ? "" : _subtypeCode;
        }
     
     
    /** Retrieves field 1510, the four digit code.
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getTypeAndSubtypeCode()
        {
        return getTypeCode() + getSubTypeCode();
        }
     
     
    /** Retrieves field 1520, the IMAD or Input Message Accountability Data.
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getIMAD()
        {
        return _imad == null ? "" : _imad;
        }
 
     
    /** Retrieves field 2000, the amount field..
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getAmount()
        {
        return _amount == null ? "" : _amount;
        }
 
     
    /** Retrieves field 3100, the Sender's Financial Institution.
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getSenderFI()
        {
        return _senderFI == null ? "" : _senderFI;
        }
     
     
    /** Retrieves field 3100, the Sender's Financial Institution 9-digit ABA number.
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getSenderABA()
        {
        String fi = getSenderFI();
        if (fi.length() > 9)
            return fi.substring(0,9);
        else if (fi.length() == 9)
            return fi;
        return "";
        }
     
     
    /** Retrieves field 3400, the Receiver's Financial Institution.
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getReceiverFI()
        {
        return _receiverFI == null ? "" : _receiverFI;
        }
 
     
    /** Retrieves field 3400, the Receiver's Financial Institution 9-digit ABA number.
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getReceiverABA()
        {
        String fi = getReceiverFI();
        if (fi.length() > 9)
            return fi.substring(0,9);
        else if (fi.length() == 9)
            return fi;
        return "";
        }
 
     
    /** Retrieves field 3600, the Business Function Code.
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getBusinessFunctionCode()
        {
        return _businessFuncCode == null ? "" : _businessFuncCode;
        }
     
     
    /** Retrieves field 1100, the Message Disposition.
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getMessageDisposition()
        {
        return _messageDisposition == null ? "" : _messageDisposition;
        }
     
     
    /** Retrieves field 1110, the Acceptance Time Stamp.
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getAcceptanceTimeStamp()
        {
        return _acceptanceTimestamp == null ? "" : _acceptanceTimestamp;
        }
     
     
    /** Retrieves field 1120, the OMAD or Output Message Accountability Data.
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getOMAD()
        {
        return _omad == null ? "" : _omad;
        }
     
     
    /** Retrieves field 1130, the Fedwire Error.
     * 
     * @return the value or empty string if it does not exist.
     */
    public String getErrorField()
        {
        return _error == null ? "" : _error;
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
 
     
    /** Attempts to return field 4000, if it exists.  This is the
     * Intermediary Financial Institution ID, Name, and Address.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getIntermediaryFI()
        {
        return getFieldValue("4000");
        }
     
     
    /** Attempts to return the ID-code and identifier from field 4000,
     * the Intermediary Financial Institution, if it exists.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getIntermediaryFIID()
        {
        String fi = getIntermediaryFI();
        if (fi.length() == 0)
            return "";
        int index = fi.indexOf("\r");
        if (index > 0)
            return fi.substring(0,index).trim();
        return fi.trim();
        }
 
     
    /** Attempts to return field 4100, if it exists.  This is the
     * Beneficiary Financial Institution ID, Name, and Address.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getBeneficiaryFI()
        {
        return getFieldValue("4100");
        }
     
     
    /** Attempts to return the ID-code and identifier from field 4100,
     * the Beneficiary Financial Institution, if it exists.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getBeneficiaryFIID()
        {
        String fi = getBeneficiaryFI();
        if (fi.length() == 0)
            return "";
        int index = fi.indexOf("\r");
        if (index > 0)
            return fi.substring(0,index).trim();
        return fi.trim();
        }
 
     
    /** Attempts to return field 4100, if it exists.  This is the
     * Beneficiary's ID, Name, and Address.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getBeneficiary()
        {
        return getFieldValue("4200");
        }
     
     
    /** Attempts to return the ID-code and identifier from field 4200,
     * the Beneficiary, if it exists.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getBeneficiaryID()
        {
        String fi = getBeneficiary();
        if (fi.length() == 0)
            return "";
        int index = fi.indexOf("\r");
        if (index > 0)
            return fi.substring(0,index).trim();
        return fi.trim();
        }
 
     
    /** Attempts to return field 4400, if it exists.  This is the
     * Account Debited in a Drawdown ID, Name, and Address.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getDrawdownDebitAccount()
        {
        return getFieldValue("4400");
        }
     
     
    /** Attempts to return the ID-code (must be D) and identifier from field 4400,
     * the Account Debitded in a Drawdown, if it exists.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getDrawdownDebitAccountID()
        {
        String fi = getDrawdownDebitAccount();
        if (fi.length() == 0)
            return "";
        int index = fi.indexOf("\r");
        if (index > 0)
            return fi.substring(0,index).trim();
        return fi.trim();
        }
 
     
    /** Attempts to return field 5100, if it exists.  This is the
     * Originator's Financial Institution ID, Name, and Address.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getOriginatorFI()
        {
        return getFieldValue("5100");
        }
     
     
    /** Attempts to return the ID-code and identifier from field 5100,
     * the Originator Financial Institution, if it exists.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getOriginatorFIID()
        {
        String fi = getOriginatorFI();
        if (fi.length() == 0)
            return "";
        int index = fi.indexOf("\r");
        if (index > 0)
            return fi.substring(0,index).trim();
        return fi.trim();
        }
 
     
    /** Attempts to return field 5000, if it exists.  This is the
     * Originator's ID, Name, and Address.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getOriginator()
        {
        return getFieldValue("5000");
        }
     
     
    /** Attempts to return the ID-code and identifier from field 5000,
     * the Originator, if it exists.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getOriginatorID()
        {
        String fi = getOriginator();
        if (fi.length() == 0)
            return "";
        int index = fi.indexOf("\r");
        if (index > 0)
            return fi.substring(0,index).trim();
        return fi.trim();
        }
 
     
    /** Attempts to return field 5100, if it exists.  This is the
     * Instructing's Financial Institution ID, Name, and Address.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getInstructingFI()
        {
        return getFieldValue("5200");
        }
     
     
    /** Attempts to return the ID-code and identifier from field 5100,
     * the Instructing Financial Institution, if it exists.
     * 
     * @return the value if found or an empty string if field does not exist.
     */
    public String getInstructingFIID()
        {
        String fi = getOriginatorFI();
        if (fi.length() == 0)
            return "";
        int index = fi.indexOf("\r");
        if (index > 0)
            return fi.substring(0,index).trim();
        return fi.trim();
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
 
    /** Decodes an advice code and returns its descriptive text.
     * 
     * @param Code the code to look up.
     * @return the description if found or an empty string if not found.
     */
    public static String lookupAdviceCode(String Code)
        {
        for (CodeLookup lookup : _adviceCode)
            {
            if (lookup.getCode().compareTo(Code) == 0)
                return lookup.getDescription();
            }
        return "";
        }
 
 
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
 
 
    /** Decodes a business function code and returns its descriptive text.
     * 
     * @param Code the code to look up.
     * @return the description if found or an empty string if not found.
     */
    public static String lookupBusinessFunction(String Code)
        {
        for (CodeLookup lookup : _businessFunction)
            {
            if (lookup.getCode().compareTo(Code) == 0)
                return lookup.getDescription();
            }
        return "";
        }
    }