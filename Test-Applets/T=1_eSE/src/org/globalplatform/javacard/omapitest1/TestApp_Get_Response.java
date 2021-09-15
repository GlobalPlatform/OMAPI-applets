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

public class TestApp_Get_Response extends Applet {
    boolean expectGetResponse;
    public static void install(byte[] aArray, short sOffset, byte bLength) {
        new TestApp_Get_Response(aArray, sOffset, bLength);
    }

    private TestApp_Get_Response(byte[] aArray, short sOffset, byte bLength) {
        register(aArray, (short) (sOffset + 1), aArray[sOffset]);
    }

    public void process(APDU apdu) throws ISOException {
        if (selectingApplet()) {
            expectGetResponse = false;
            return;
        }

        byte[] buffer = apdu.getBuffer();
        short p1 = buffer[ISO7816.OFFSET_P1];
        short p2 = buffer[ISO7816.OFFSET_P2];
        short le;

        if (p1 != (byte) 0x00 || p2 != (byte) 0x00) {
            ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
        }

        if (buffer[ISO7816.OFFSET_LC] != (byte) 0x04) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }

        switch (buffer[ISO7816.OFFSET_INS]) {
        case (byte) 0x40: // APDU_case8 (CASE8)
            expectGetResponse = true;
            ISOException.throwIt((short) 0x62F1);

        case (byte) 0xC0: // Get Response
            if(!expectGetResponse) {
                ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
            }
            expectGetResponse = false;
            buffer[0] = (byte) 0x01;
            buffer[1] = (byte) 0x02;
            buffer[2] = (byte) 0x03;
            buffer[3] = (byte) 0x04;
            le = 4;
            apdu.setOutgoingAndSend((short) 0, le);      
            ISOException.throwIt(ISO7816.SW_NO_ERROR);
        default:
            ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }
}
