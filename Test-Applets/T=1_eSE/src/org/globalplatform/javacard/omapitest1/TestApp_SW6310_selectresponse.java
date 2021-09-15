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

public class TestApp_SW6310_selectresponse extends Applet {

    public static void install(byte[] aArray, short sOffset, byte bLength) {
        new TestApp_SW6310_selectresponse(aArray, sOffset, bLength);
    }

    private TestApp_SW6310_selectresponse(byte[] aArray, short sOffset,
            byte bLength) {
        register(aArray, (short) (sOffset + 1), aArray[sOffset]);
    }

    public void process(APDU apdu) throws ISOException {
        byte[] buffer = apdu.getBuffer();

        if (selectingApplet()) {
 			buffer[0] = (byte) 0xDE;
            buffer[1] = (byte) 0xAD;
            buffer[2] = (byte) 0xC0;
            buffer[3] = (byte) 0xDE;
            apdu.setOutgoingAndSend((short) 0, (short) 4);
            ISOException.throwIt((short) 0x6310);
		}

 		switch (buffer[ISO7816.OFFSET_INS]) {
        case (byte) 0x10: // Test-APDU1 (CASE4)
            short lc = apdu.setIncomingAndReceive();
            Util.arrayCopyNonAtomic(buffer, ISO7816.OFFSET_CDATA, buffer, (short) 0, lc);
            apdu.setOutgoingAndSend((short) 0, lc);
            break;
        default:
            ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }
}
