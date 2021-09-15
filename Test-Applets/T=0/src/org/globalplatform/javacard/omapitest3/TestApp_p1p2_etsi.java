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

package org.globalplatform.javacard.omapitest3;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.Util;
import javacard.framework.JCSystem;

public class TestApp_p1p2_etsi extends Applet {

    private boolean mIsExpectingGetResponse = false;
    private boolean mIsFirstGetResponse = false;

    private short[] sw1sw2_table = {
	ISO7816.SW_WRONG_DATA, // 0x00
	(short) 0x6200, // 0x01 START WARNING.....
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
	(short) 0x63F2, // 0x11 END OF WARNING...
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

    public static void install(byte[] aArray, short sOffset, byte bLength)
    {
        new TestApp_p1p2_etsi(aArray, sOffset, bLength);
    }

    private TestApp_p1p2_etsi(byte[] aArray, short sOffset, byte bLength)
    {
	mIsExpectingGetResponse = false;
	mIsFirstGetResponse = false;
        register(aArray, (short) (sOffset + 1), aArray[sOffset]);
    }

    public void process(APDU apdu) throws ISOException
    {
        if (selectingApplet()) 
	    return;

	byte[] buffer = apdu.getBuffer();

	if (mIsExpectingGetResponse)   {    
	    // this flag is for CASE4 instruction.
	    // ----------------------------------
	    if (buffer[ISO7816.OFFSET_INS] == (byte)0xC0) {

		if (mIsFirstGetResponse) {
		    // ETSI compliant Terminal shall issue  GET-RESPONSE with LE = 0x00
		    // ----------------------------------------------------------------
		    mIsFirstGetResponse = false;

		    if (buffer[ISO7816.OFFSET_LC] == (byte)0) {
			// Terminal shall reissue get-response with the result of 61XX
			// Let's continue.
			ISOException.throwIt((short)0x61FF);
		    }
		    // error.
		    mIsExpectingGetResponse = false;
		    ISOException.throwIt(ISO7816.SW_WRONG_LENGTH); // 6700
		}

		// mIsFirstGetResponse is false

		if (buffer[ISO7816.OFFSET_LC] != (byte)0xFF)  {
		    // this is an error because ETSI compliant terminal
		    // shall reissue get-response with the result of 61XX
		    mIsExpectingGetResponse = false;
		    ISOException.throwIt(ISO7816.SW_WRONG_LENGTH); // 6700
		}
		
		// OK return the data.
		// ------------------

                short le = (short)255;
                for (short i = 0; i <= le; i++) {
                    buffer[i] = (byte) (i + (short) 1);
                }

                apdu.setOutgoingAndSend((short) 0, le);
		mIsExpectingGetResponse = false;
		// finish OK.
		ISOException.throwIt((short)0x9000);
	    } 

	    // unexpected
	    mIsExpectingGetResponse = false;
	    mIsFirstGetResponse = false;
	    ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED); // 6D00
	}

        short p1 = buffer[ISO7816.OFFSET_P1];
        short p2 = buffer[ISO7816.OFFSET_P2];

	switch (buffer[ISO7816.OFFSET_INS]) {

        case (byte) 0x01: { // APDU_case1 (CASE1)
            if (p1 > (byte) 0x32) {
                ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
            }
            ISOException.throwIt(sw1sw2_table[p1]);
	}
        case (byte) 0x02:{ // APDU_case2 (CASE2)
            if (p1 < (byte) 0x12) {
                short le = 0xFF;
                for (short i = 0; i <= le; i++) {
                    buffer[i] = (byte) (i + (short) 1);
                }

                apdu.setOutgoingAndSend((short) 0, le);
                ISOException.throwIt(sw1sw2_table[p1]);
            } else if (p1 <= (byte)0x32) {
                ISOException.throwIt(sw1sw2_table[p1]);
            } else {
                ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
            }
	}
        case (byte) 0x03: { // APDU_case3 (CASE3)
            short lc = apdu.setIncomingAndReceive();
	    if (lc == 0) // normally != 255 for ID34 and 35
		ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
            if (p1 > (byte) 0x32) {
                ISOException.throwIt(ISO7816.SW_WRONG_P1P2);
            }
            ISOException.throwIt(sw1sw2_table[p1]);
	}
        case (byte) 0x04: { // APDU_case4 (CASE4)
            short lc = apdu.setIncomingAndReceive();
	    if (lc == 0) // normally != 255 for ID34 and 35
		ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
	    short le = apdu.setOutgoing();
	    if( le != 256)
		// case4 over T=0 , le = 256
		ISOException.throwIt(ISO7816.SW_COMMAND_NOT_ALLOWED);

            if (p1 < (byte)0x12) {
		// ETSI issue first WARNING.
		// and DATA on GET-RESPONSE.
                mIsExpectingGetResponse = true;
		mIsFirstGetResponse = true;
		// THROW THE WARNING.
		ISOException.throwIt(sw1sw2_table[p1]);
            } else if (p1 <= (byte) 0x32) {
		// just ISSUE SW which is not a warning and no data).
                ISOException.throwIt(sw1sw2_table[p1]);
            } else {
                ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
            }
	}
	default:
	    ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
	}
    }
}
