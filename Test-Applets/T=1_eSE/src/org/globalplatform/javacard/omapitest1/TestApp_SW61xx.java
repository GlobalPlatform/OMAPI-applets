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

import javacard.framework.*;

public class TestApp_SW61xx extends Applet {

	private byte[] outBuffer;

	public static void install(byte[] aArray, short sOffset, byte bLength) {
		new TestApp_SW61xx(aArray, sOffset, bLength);
	}

	private TestApp_SW61xx(byte[] aArray, short sOffset, byte bLength) {
		
		try
	    {
	        outBuffer = JCSystem.makeTransientByteArray( (short)300, JCSystem.CLEAR_ON_RESET );
	    }
	    catch( Exception e )
	    {
	        outBuffer = new byte[300];
	    }
	    	    
		register(aArray, (short) (sOffset + 1), aArray[sOffset]);
	}

	public void process(APDU apdu) throws ISOException {

		byte buffer[] = apdu.getBuffer();

 		if (selectingApplet())
 			return;

		byte inBuffer[] = apdu.getBuffer();
		short le, lc = (short)0;

		switch (inBuffer[ISO7816.OFFSET_INS]) {
		case (byte) 0x40: // Test-APDU5 (CASE2) but force a 61xx response
			ISOException.throwIt((short)0x6104);
		
		case (byte) 0xC0: // faked GET RESPONSE response
			le = (short)(0xff &  inBuffer[ISO7816.OFFSET_LC]);
			if (le == (short) 0x00)
				le = (short) 0x100;
			
			le = 0x04;	// actually I don't understand the command definition

			for (short i=0; i<=le; i++)
				inBuffer[i] = (byte)(i+(short)1);

			apdu.setOutgoingAndSend((short)0, le);
			break;

		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
}
