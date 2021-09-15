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
import javacard.framework.Util;

public class TestApp_Multi_SW61xx extends Applet {

    private byte[] mGetResponseData;
    private byte mIteration;
    public static void install(byte[] aArray, short sOffset, byte bLength) {
        new TestApp_Multi_SW61xx(aArray, sOffset, bLength);
    }

    private TestApp_Multi_SW61xx(byte[] aArray, short sOffset, byte bLength) {
        register(aArray, (short) (sOffset + 1), aArray[sOffset]);
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
            case (byte) 0x40: // APDU Long Response
                if (p1 != (byte) 0x20 || p2 != (byte) 0x00) {
                    ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
                }
                mGetResponseData = new byte[32];
                mIteration = (byte) 0x00;
                ISOException.throwIt((short) 0x6120);

            case (byte) 0xC0: // Get Response
                if (p1 != (byte) 0x00 || p2 != (byte) 0x00) {
                    ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
                }
                le = buffer[ISO7816.OFFSET_LC];

                if (le != (byte) 0x20) { // Le must be 32 bytes
                    ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
                }

                try {
                    Util.arrayCopyNonAtomic( // Response current byte[]
                            mGetResponseData, (short) 0, buffer, (short) 0, le);
                } catch(Exception e) {
                    ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                }

                apdu.setOutgoingAndSend((short) 0, le);

                if(mIteration == (byte) 0x09) { // SW
                    mGetResponseData = null;
                    ISOException.throwIt(ISO7816.SW_NO_ERROR);
                } else {
                    ++mIteration;
                    Util.arrayFillNonAtomic(mGetResponseData,
                            (short) 0, (short) 32,
                            (byte) (mIteration << 4 | mIteration));
                    ISOException.throwIt((short) 0x6120);
                }
            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }
}
