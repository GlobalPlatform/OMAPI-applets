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

public class TestApp_Case4_SWwarning extends Applet {
	private static final byte INS_THROW_WARNING = (byte) 0x11;
	private static final byte INS_GET_RESPONSE  = (byte) 0xC0;
	private static final byte P1_6280 = 0x03;
	private static final byte P1_6283 = 0x06;
	private static final byte P1_6310 = 0x0E;
	private static final byte P1_63C2 = 0x0F;
	
	private boolean mIsExpectingGetResponse;
	
	public static void install(byte[] aArray, short sOffset, byte bLength) {
        new TestApp_Case4_SWwarning(aArray, sOffset, bLength);
    }
	
	private TestApp_Case4_SWwarning(byte[] aArray, short sOffset, byte bLength) {
		register(aArray, (short) (sOffset + 1), aArray[sOffset]);
		mIsExpectingGetResponse = false;
	}

	public void process(APDU apdu) throws ISOException {

        if (selectingApplet()) {
            return;
        }

        byte[] inBuffer = apdu.getBuffer();
        
        switch(inBuffer[ISO7816.OFFSET_INS]) {
        case INS_THROW_WARNING:
        	mIsExpectingGetResponse = false;
        	apdu.setIncomingAndReceive();
        	if (inBuffer[ISO7816.OFFSET_P2] != 0x00) {
        		throw new ISOException(ISO7816.SW_WRONG_P1P2);
        	}
        	switch(inBuffer[ISO7816.OFFSET_P1]) {
        	case P1_6280:
        		mIsExpectingGetResponse = true;
        		throw new ISOException((short) 0x6280);
        	case P1_6283:
        		mIsExpectingGetResponse = true;
        		throw new ISOException((short) 0x6283);
        	case P1_6310:
        		mIsExpectingGetResponse = true;
        		throw new ISOException((short) 0x6310);
        	case P1_63C2:
        		mIsExpectingGetResponse = true;
        		throw new ISOException((short) 0x63C2);
    		default:
    			throw new ISOException(ISO7816.SW_WRONG_P1P2);
        	}
        case INS_GET_RESPONSE:
        	if (inBuffer[ISO7816.OFFSET_P1] != 0x00 || inBuffer[ISO7816.OFFSET_P2] != 0x00) {
        		throw new ISOException(ISO7816.SW_WRONG_P1P2);
        	}
        	if (mIsExpectingGetResponse) {
        		mIsExpectingGetResponse = false;
        		inBuffer[0] = (byte) 0xDE;
        		inBuffer[1] = (byte) 0xAD;
        		inBuffer[2] = (byte) 0xC0;
        		inBuffer[3] = (byte) 0xDE;
        		apdu.setOutgoingAndSend((short) 0, (short) 4);
        	} else {
        		ISOException.throwIt(ISO7816.SW_UNKNOWN);
        	}
        	break;
    	default:
    		mIsExpectingGetResponse = false;
    		throw new ISOException(ISO7816.SW_FUNC_NOT_SUPPORTED);
        }

    }	
}
