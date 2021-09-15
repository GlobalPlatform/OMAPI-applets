/*********************************************************************************
 Copyright 2018 GlobalPlatform, Inc. 
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

package org.globalplatform.javacard.omapitest1;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.JCSystem;
import javacard.framework.Util;

public class TestApp_p1p2 extends Applet {
    private boolean mIsExpectingGetResponse = false;
    private byte[] mGetResponseData;
    private short mGetResponseSw;

    private short[] sw1sw2_table = {
            ISO7816.SW_WRONG_DATA, // 0x00
            (short) 0x6200,
            (short) 0x6202,
            (short) 0x6280,
            (short) 0x6281, // 0x04
            (short) 0x6282,
            (short) 0x6283,
            (short) 0x6284,
            (short) 0x6285, // 0x08
            (short) 0x6286,
            (short) 0x62F1,
            (short) 0x62F2,
            (short) 0x6300, // 0x0C
            (short) 0x6381,
            (short) 0x63C2,
            (short) 0x6310,
            (short) 0x63F1, // 0x10
            (short) 0x63F2,
            (short) 0x6400,
            (short) 0x6401,
            (short) 0x6402,// 0x14
            (short) 0x6480,
            (short) 0x6500,
            (short) 0x6581,
            (short) 0x6800, // 0x18
            (short) 0x6881,
            (short) 0x6882,
            (short) 0x6883,
            (short) 0x6884, // 0x1C
            (short) 0x6900,
            (short) 0x6900,
            (short) 0x6981,
            (short) 0x6982, // 0x20
            (short) 0x6983,
            (short) 0x6984,
            (short) 0x6985,
            (short) 0x6986, // 0x24
            (short) 0x6987,
            (short) 0x6988,
            (short) 0x6A00,
            (short) 0x6A80, // 0x28
            (short) 0x6A81,
            (short) 0x6A82,
            (short) 0x6A83,
            (short) 0x6A84, // 0x2C
            (short) 0x6A85,
            (short) 0x6A86,
            (short) 0x6A87,
            (short) 0x6A88, // 0x30
            (short) 0x6A89,
            (short) 0x6A8A
        };

    public static void install(byte[] aArray, short sOffset, byte bLength) {
        new TestApp_p1p2(aArray, sOffset, bLength);
    }

    private TestApp_p1p2(byte[] aArray, short sOffset, byte bLength) {
        register(aArray, (short) (sOffset + 1), aArray[sOffset]);
        mGetResponseData = JCSystem.makeTransientByteArray(
                                    (short) 300,
                                    JCSystem.CLEAR_ON_RESET);
    }

    public void process(APDU apdu) throws ISOException {
        if (selectingApplet()) {
            return;
        }

        byte[] buffer = apdu.getBuffer();
        short p1 = buffer[ISO7816.OFFSET_P1];
        short p2 = buffer[ISO7816.OFFSET_P2];
        short le;

        switch (buffer[ISO7816.OFFSET_INS]) {
        case (byte) 0xC0: // Get Response
            if (!mIsExpectingGetResponse) {
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
            }
            if (p1 != (short) 0x00 || p2 != (short) 0x00) {
                ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
            }
            mIsExpectingGetResponse = false;
            for (short i = 0; i < 0xFF; i++) {
                buffer[i] = (byte) (i + (short) 1);
            }
            apdu.setOutgoingAndSend((short) 0, (short) 0xFF);
            ISOException.throwIt(mGetResponseSw);
        case (byte) 0x01: // APDU_case1 (CASE1)
            if (p1 > (byte) 0x32) {
                ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
            }
            ISOException.throwIt(sw1sw2_table[p1]);

        case (byte) 0x02: // APDU_case2 (CASE2)
            if (p1 < (byte) 0x12) {
                le = 0xFF;
                for (short i = 0; i <= le; i++) {
                    buffer[i] = (byte) (i + (short) 1);
                }

                apdu.setOutgoingAndSend((short) 0, le);
                ISOException.throwIt(sw1sw2_table[p1]);
            } else if (p1 <= (byte) 0x32) {
                ISOException.throwIt(sw1sw2_table[p1]);
            } else {
                ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
            }

        case (byte) 0x03: // APDU_case3 (CASE3)
            apdu.setIncomingAndReceive();
            if (p1 > (byte) 0x32) {
                ISOException.throwIt(ISO7816.SW_WRONG_DATA);
            }
            ISOException.throwIt(sw1sw2_table[p1]);

        case (byte) 0x04: // APDU_case4 (CASE4)
        	apdu.setIncomingAndReceive();
            if (p1 < (byte) 0x12) {
                mIsExpectingGetResponse = true;
                mGetResponseSw = sw1sw2_table[p1];
                ISOException.throwIt((short) 0x61FF);
            } else if (p1 <= (byte) 0x32) {
                ISOException.throwIt(sw1sw2_table[p1]);
            } else {
                ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
            }

        default:
            ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }
}
