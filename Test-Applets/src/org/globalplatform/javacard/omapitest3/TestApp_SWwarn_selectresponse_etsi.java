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

public class TestApp_SWwarn_selectresponse_etsi extends Applet {

    private boolean mIsExpectingGetResponse = false;
    private boolean mIsFirstGetResponse = false;
    private short   mGetResponseDataLength = (short)0;
    private short   mWarning = (short)0;
    private byte[]  mGetResponseData;
    

    protected TestApp_SWwarn_selectresponse_etsi(short warning)
    {
	mIsExpectingGetResponse = false;
	mIsFirstGetResponse = false;
	mGetResponseDataLength = (short)0;
	mWarning = warning;

	mGetResponseData = JCSystem.makeTransientByteArray((short)64,
							   JCSystem.CLEAR_ON_RESET);
    }

    public void process(APDU apdu) throws ISOException
    {
        byte[] buffer = apdu.getBuffer();

        if (selectingApplet()) {
	    // This (ETSI) card FIRST issue a warning
	    // and wait for GET-RESPONSE  with LE=00

	    mIsExpectingGetResponse = true;
	    mIsFirstGetResponse = true;
	    mGetResponseDataLength = (short)4;

	    mGetResponseData[0] = (byte) 0xDE;
	    mGetResponseData[1] = (byte) 0xAD;
	    mGetResponseData[2] = (byte) 0xC0;
	    mGetResponseData[3] = (byte) 0xDE;
	    if (buffer[ISO7816.OFFSET_P2] != (byte)0x00) {
		mGetResponseData[4] = buffer[ISO7816.OFFSET_P2];
		mGetResponseDataLength = (short)5;
	    }
	    ISOException.throwIt(mWarning);
	}
 		
	if (mIsExpectingGetResponse)   {    

	    if (buffer[ISO7816.OFFSET_INS] == (byte)0xC0) {

		if (mIsFirstGetResponse) {
		    // ETSI compliant Terminal shall issue  GET-RESPONSE with LE = 0x00
		    // --------
		    mIsFirstGetResponse = false;

		    if (buffer[ISO7816.OFFSET_LC] == (byte)0) {
			// Terminal shall reissue get-response with the result of 61XX
			// Let's continue.
			ISOException.throwIt((short)  (0x6100 + mGetResponseDataLength));
		    }
		    // error.
		    mIsExpectingGetResponse = false;
		    ISOException.throwIt(ISO7816.SW_WRONG_LENGTH); // 6700
		}

		// mIsFirstGetResponse is false

		if (buffer[ISO7816.OFFSET_LC] != (byte)mGetResponseDataLength)  {
		    // this is an error because ETSI compliant terminal
		    // shall reissue get-response with the result of 61XX
		    mIsExpectingGetResponse = false;
		    mGetResponseDataLength = (short)0;
		    ISOException.throwIt(ISO7816.SW_WRONG_LENGTH); // 6700
		}
		
		// OK return the data.
		// ------------------
		Util.arrayCopyNonAtomic(mGetResponseData, (short)0, buffer, (short) 0,  mGetResponseDataLength);
		apdu.setOutgoingAndSend((short) 0,  mGetResponseDataLength);

		mIsExpectingGetResponse = false;
		mGetResponseDataLength = (short)0;
		ISOException.throwIt((short)0x9000);
	    } 

	    // unexpected
	    mIsExpectingGetResponse = false;
	    mIsFirstGetResponse = false;
	    mGetResponseDataLength = (short)0;

	    ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED); // 6D00
	}

	if (buffer[ISO7816.OFFSET_INS] == (byte) 0x11) {

	    // CASE4_WARNING and NO DATA.
	    // transmit: ID30 -> ID33,    ID36 -> ID39
	    // if the Terminal issues a GET-RESPONSE it is an error.
	    mIsExpectingGetResponse = false;
	    mIsFirstGetResponse = false;
	    mGetResponseDataLength = (short)0;

	    short lc = apdu.setIncomingAndReceive();
	    if (lc != 255)
		ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);

	    switch (buffer[ISO7816.OFFSET_P1]) {
	    case (byte)0x03:
		ISOException.throwIt((short)0x6280);
	    case (byte)0x06:
		ISOException.throwIt((short)0x6283);
	    case (byte)0x0E:
		ISOException.throwIt((short)0x6310);
	    case (byte)0x0F:
		ISOException.throwIt((short)0x63C2);
	    default:
		ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
	    }
	} else  {
	    ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
	}

// 	    case  (byte)0x03: {
// 		    // Test-APDU1 (CASE4)
// 		    short lc = apdu.setIncomingAndReceive();
// 		    // get the LE
// 		    short le = apdu.setOutgoing();
// 		    short result_of_setoutgoing = le;

// 		    if (le ==(short)0)
// 			le = 256;
// 		    if (le > lc)
// 			le = lc;

// 		    mIsExpectingGetResponse = true;
// 		    mIsFirstGetResponse = true;
// 		    mGetResponseDataLength = le; // (short) (le + 2);

// 		    Util.arrayCopyNonAtomic(buffer, ISO7816.OFFSET_CDATA, mGetResponseData, 
// 					    (short)0 /* 2 */, le);
// 		    // mGetResponseData[0] = (byte)(result_of_setoutgoing / 256);
// 		    // mGetResponseData[1] = (byte)(result_of_setoutgoing % 256);
// 		    // issue warning.
// 		    ISOException.throwIt((short) 0x6283);
// 		    break;
// 		}
// 	    default:
// 		ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
// 	    }
    }
}
