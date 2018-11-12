/*********************************************************************************
 Copyright 2017 GlobalPlatform, Inc. 

 Licensed under the GlobalPlatform/Apache License, Version 2.0 (the "License"); 
 you may not use this file except in compliance with the License. 
 You may obtain a copy of the License at 

 https://github.com/GlobalPlatform/OMAPI_Test_Spec/blob/master/GlobalPlatform%20Apache%20License.pdf

 Unless required by applicable law or agreed to in writing, software 
 distributed under the License is distributed on an "AS IS" BASIS, 
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 implied. 
 See the License for the specific language governing permissions and 
 limitations under the License. 
*********************************************************************************/

/*********************************************************************************
 *   CHANGE HISTORY:
 *
 * 2017/11/08:
 * 	- First version.
 * 2018/05/08:
 *	- updated content of RESPONSE_GET_ALL (fixed length on 1 byte issue)
 *
 ********************************************************************************/

package org.globalplatform.test.ara;

import javacard.framework.*;

public class ARAapplet extends Applet implements MultiSelectable {

    static final byte INS_GET_DATA = (byte) 0xCA;
    static final short TAG_CMD_GET_NEXT = (short) 0xFF60;
    static final short TAG_CMD_GET_SPECIFIC = (short) 0xFF50;
    static final short TAG_CMD_GET_ALL = (short) 0xFF40;
    static final short TAG_CMD_GET_REFRESH = (short) 0xDF20;
    private final static byte[] RESPONSE_GET_REFRESH = { (byte) 0xDF,
            (byte) 0x20, (byte) 0x08, (byte) 0x01, (byte) 0x02, (byte) 0x03,
            (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08 };
    private final static byte[] COMMAND_GET_SPECIFIC_DENIED = { (byte) 0x80,
            (byte) 0xCA, (byte) 0xFF, (byte) 0x50, (byte) 0x11, (byte) 0xE1,
            (byte) 0x0F, (byte) 0x4F, (byte) 0x0B, (byte) 0xA0, (byte) 0x00,
            (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x01, (byte) 0x00,
            (byte) 0x01, (byte) 0xEE, (byte) 0x05, (byte) 0xFE, (byte) 0xC1,
            (byte) 0x00, (byte) 0x00 };
    private final static byte[] RESPONSE_GET_SPECIFIC_DENIED = { (byte) 0xFF,
            (byte) 0x50, (byte) 0x08, (byte) 0xE3, (byte) 0x06, (byte) 0xD0,
            (byte) 0x01, (byte) 0x00, (byte) 0xD1, (byte) 0x01, (byte) 0x00 };
    private final static byte[] COMMAND_GET_SPECIFIC_TestApp = { (byte) 0x80,
            (byte) 0xCA, (byte) 0xFF, (byte) 0x50, (byte) 0x11, (byte) 0xE1,
            (byte) 0x0F, (byte) 0x4F, (byte) 0x0B, (byte) 0xA0, (byte) 0x00,
            (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x01, (byte) 0x00,
            (byte) 0x01, (byte) 0xEE, (byte) 0x05, (byte) 0x01, (byte) 0xC1,
            (byte) 0x00, (byte) 0x00 };
    private final static byte[] RESPONSE_GET_SPECIFIC_TestApp = {
            (byte) 0xFF, (byte) 0x50, (byte) 0x67,
                (byte) 0xE3, (byte) 0x65,
                    (byte) 0xD0, (byte) 0x60,
                        (byte) 0x00, (byte) 0x10, (byte) 0x01, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0x00, (byte) 0x10, (byte) 0x02, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0x00, (byte) 0x30, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0x00, (byte) 0x40, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xEF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0x00, (byte) 0x55, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0x00, (byte) 0xA4, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0xFB, (byte) 0xFF,
                        (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0x7F, (byte) 0xE0,
												//Rule for APDU_INV_LC_INF_case3 and APDU_INV_LC_SUP_case3
                        (byte) 0x00, (byte) 0x50, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
												//Rule for APDU_INV_LC_INF_case4 and APDU_INV_LC_SUP_case4
                        (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
												//Rule for Invalid APDU_with CLA ‘FF’
                        (byte) 0xFF, (byte) 0x30, (byte) 0x00, (byte) 0x00,
                        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
												//Rule for Invalid APDU_with INS ‘0x60 to 0x6F
                        (byte) 0x00, (byte) 0x60, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xF0, (byte) 0xFF, (byte) 0xFF,
												//Rule for Invalid APDU_with INS ‘0x90 to 0x9F
                        (byte) 0x00, (byte) 0x90, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xF0, (byte) 0xFF, (byte) 0xFF,
                    (byte) 0xD1, (byte) 0x01, (byte) 0x00 };
    private final static byte[] RESPONSE_GET_SPECIFIC_OTHERS = { (byte) 0xFF,
            (byte) 0x50, (byte) 0x08, (byte) 0xE3, (byte) 0x06, (byte) 0xD0,
            (byte) 0x01, (byte) 0x01, (byte) 0xD1, (byte) 0x01, (byte) 0x01 };
    private final static byte[] RESPONSE_GET_ALL = {
        (byte) 0xFF, (byte) 0x40, (byte) 0x81, (byte) 0xA5,
            // Rules for AID A0 00 00 06 00 01 00 01 EE 05 FE
            (byte) 0xE2, (byte) 0x19,
                (byte) 0xE1, (byte) 0x0F,
                    (byte) 0x4F, (byte) 0x0B,
                        (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x06,
                        (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01,
                        (byte) 0xEE, (byte) 0x05, (byte) 0xFE,
                    (byte) 0xC1, (byte) 0x00,
                (byte) 0xE3, (byte) 0x06,
                    (byte) 0xD0, (byte) 0x01, (byte) 0x00,
                    (byte) 0xD1, (byte) 0x01, (byte) 0x00,
            // Rules for AID A0 00 00 06 00 01 00 01 EE 05 01
            (byte) 0xE2, (byte) 0x78,
                (byte) 0xE1, (byte) 0x0F,
                    (byte) 0x4F, (byte) 0x0B,
                        (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x06,
                        (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01,
                        (byte) 0xEE, (byte) 0x05, (byte) 0x01,
                    (byte) 0xC1, (byte) 0x00,
                (byte) 0xE3, (byte) 0x65,
                    (byte) 0xD0, (byte) 0x60,
                        (byte) 0x00, (byte) 0x10, (byte) 0x01, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0x00, (byte) 0x10, (byte) 0x02, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0x00, (byte) 0x30, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0x00, (byte) 0x40, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xEF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0x00, (byte) 0x55, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0x00, (byte) 0xA4, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0xFB, (byte) 0xFF,
                        (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0x7F, (byte) 0xE0,
												//Rule for APDU_INV_LC_INF_case3 and APDU_INV_LC_SUP_case3
                        (byte) 0x00, (byte) 0x50, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
												//Rule for APDU_INV_LC_INF_case4 and APDU_INV_LC_SUP_case4
                        (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
												//Rule for Invalid APDU_with CLA ‘FF’
                        (byte) 0xFF, (byte) 0x30, (byte) 0x00, (byte) 0x00,
                        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
												//Rule for Invalid APDU_with INS ‘0x60 to 0x6F
                        (byte) 0x00, (byte) 0x60, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xF0, (byte) 0xFF, (byte) 0xFF,
												//Rule for Invalid APDU_with INS ‘0x90 to 0x9F
                        (byte) 0x00, (byte) 0x90, (byte) 0x00, (byte) 0x00,
                        (byte) 0xF0, (byte) 0xF0, (byte) 0xFF, (byte) 0xFF,
                    (byte) 0xD1, (byte) 0x01, (byte) 0x00,
            // Rules for all applets (allow all)
            (byte) 0xE2, (byte) 0x0E,
                (byte) 0xE1, (byte) 0x04,
                    (byte) 0x4F, (byte) 0x00,
                    (byte) 0xC1, (byte) 0x00,
                (byte) 0xE3, (byte) 0x06,
                    (byte) 0xD0, (byte) 0x01, (byte) 0x01,
                    (byte) 0xD1, (byte) 0x01, (byte) 0x01
    };

    public static void install(byte[] abArray, short sOffset, byte bLength) {
        (new ARAapplet()).register(abArray, (short) (sOffset + 1), abArray[sOffset]);
    }

    public boolean select(boolean appInstAlreadyActive) {
        return true;
    }

    public void deselect(boolean appInstStillActive) {
        return;
    }

    public void process(APDU oAPDU) throws ISOException {
        short sSW1SW2 = ISO7816.SW_NO_ERROR;
        short sOutLength = (short) 0;

        byte[] abData = oAPDU.getBuffer();

        byte bINS = abData[ISO7816.OFFSET_INS];

        short sMode = Util.getShort(abData, ISO7816.OFFSET_P1);

        if (selectingApplet() == true) {
            return;
        }

        try {
            if (bINS != INS_GET_DATA) {
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
            }

            if (sMode == TAG_CMD_GET_ALL) {
                sOutLength = Util.arrayCopyNonAtomic(RESPONSE_GET_ALL,
                        (short) 0,
                        abData,
                        (short) 0,
                        (short) RESPONSE_GET_ALL.length);
            } else if (sMode == TAG_CMD_GET_SPECIFIC) {
                short numBytes = oAPDU.setIncomingAndReceive();
                if (Util.arrayCompare(abData, (short) 0, COMMAND_GET_SPECIFIC_DENIED, (short) 0, numBytes) == 0) {
                    sOutLength = Util.arrayCopyNonAtomic(RESPONSE_GET_SPECIFIC_DENIED,
                            (short) 0,
                            abData,
                            (short) 0,
                            (short) RESPONSE_GET_SPECIFIC_DENIED.length);
                } else if(Util.arrayCompare(abData, (short) 0, COMMAND_GET_SPECIFIC_TestApp, (short) 0, numBytes) == 0){
                     sOutLength = Util.arrayCopyNonAtomic(RESPONSE_GET_SPECIFIC_TestApp,
                            (short) 0,
                            abData,
                            (short) 0,
                            (short) RESPONSE_GET_SPECIFIC_TestApp.length);    
                } else { 
                    sOutLength = Util.arrayCopyNonAtomic(RESPONSE_GET_SPECIFIC_OTHERS,
                            (short) 0,
                            abData,
                            (short) 0,
                            (short) RESPONSE_GET_SPECIFIC_OTHERS.length);
                }
            } else if (sMode == TAG_CMD_GET_NEXT) {
                ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
            } else if (sMode == TAG_CMD_GET_REFRESH) {
                sOutLength = Util.arrayCopyNonAtomic(RESPONSE_GET_REFRESH,
                        (short) 0,
                        abData,
                        (short) 0,
                        (short) RESPONSE_GET_REFRESH.length);
            } else {
                ISOException.throwIt(ISO7816.SW_WRONG_P1P2);
            }
        } catch (ISOException e) {
            sSW1SW2 = e.getReason();
        } catch (Exception e) {
            sSW1SW2 = ISO7816.SW_UNKNOWN;
        }

        if (sSW1SW2 != ISO7816.SW_NO_ERROR) {
            ISOException.throwIt(sSW1SW2);
        }

        if (sOutLength > (short) 0) {
            oAPDU.setOutgoingAndSend((short) 0, sOutLength);
        }
    }
}

