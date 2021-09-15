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

package org.globalplatform.javacard.omapitest4;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.Util;
import javacard.framework.JCSystem;

public class TestApp_length extends Applet {


    public static void install(byte[] aArray, short sOffset, byte bLength)
    {
        new TestApp_length(aArray, sOffset, bLength);
    }

    private TestApp_length(byte[] aArray, short sOffset, byte bLength)
    {
        register(aArray, (short) (sOffset + 1), aArray[sOffset]);
    }

    public void process(APDU apdu) throws ISOException
    {
        byte[] buffer = apdu.getBuffer();

        if (selectingApplet()) {
	    // normal return.
	    return;
	}
	if (buffer[ISO7816.OFFSET_INS] == (byte) 0x10) {

	    if(buffer[ISO7816.OFFSET_P1] != (byte) 0x01 ||
	       buffer[ISO7816.OFFSET_P2] != (byte) 0x00)
		ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2); // 6A86

	    // CASE4
	    // data = 01 02 03 04   LE = 00
	    // --------------------------
	    short lc = apdu.setIncomingAndReceive();
	    if (lc == 0)
		// expect data.
		ISOException.throwIt(ISO7816.SW_WRONG_DATA);  // 6A80
	    short le = apdu.setOutgoing();
	    if (le != 256)
		ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);  // 6700
	    // echo the input
	    Util.arrayCopyNonAtomic(buffer, ISO7816.OFFSET_CDATA, buffer, (short)0, lc);
	    apdu.setOutgoingLength(lc);
	    apdu.sendBytes((short) 0, lc);

	} else  {
	    ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
	}
    }
}
