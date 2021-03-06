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

package org.globalplatform.javacard.omapitest2;

import javacard.framework.*;

public class TestApp_multiselectable extends Applet implements MultiSelectable {

	private byte[] outBuffer;


	public static void install(byte[] aArray, short sOffset, byte bLength) {
		new TestApp_multiselectable(aArray, sOffset, bLength);
	}

	private TestApp_multiselectable(byte[] aArray, short sOffset, byte bLength) {
		
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

	public boolean select(boolean appInstAlreadyActive)
	{
	   return true;
	}

	public void deselect(boolean appInstStillActive)
	{
	   return;
	}

	public void process(APDU apdu) throws ISOException {

 		if (selectingApplet())
			return;

		byte inBuffer[] = apdu.getBuffer();
		short le, lc = (short)0;

		switch (inBuffer[ISO7816.OFFSET_INS]) {
		case (byte) 0x10: // Test-APDU1 (CASE4)
			switch (inBuffer[ISO7816.OFFSET_P1]) {
				case 0x01:	// echo
					break;
				case 0x02:	// echo with more than 1sec delay
					for (short i=0; i<(short)0x7FFF; i++)
						APDU.waitExtension();
					break;
				default:
					ISOException.throwIt(ISO7816.SW_WRONG_DATA);
			}

			lc = apdu.setIncomingAndReceive();

			Util.arrayCopyNonAtomic(inBuffer, ISO7816.OFFSET_CDATA, outBuffer, (short)0, lc);
	
			Util.arrayCopyNonAtomic(outBuffer, ( short ) 0, inBuffer, ( short ) 0, lc );
		
			apdu.setOutgoingAndSend( ( short ) 0, (short)lc );
			
			break;

		case (byte) 0x30: // Test-APDU4 (CASE1)
			apdu.setOutgoingAndSend((short) 0, (short) 0);
			break;
			
		case (byte) 0x40: // Test-APDU5 (CASE2)
			le = (short)(0xff &  inBuffer[ISO7816.OFFSET_LC]);
			if (le == (short) 0x00)
				le = (short) 0x100;
			
			// I don't understand the command definition
			le = 0x04;

			for (short i=0; i<=le; i++)
				inBuffer[i] = (byte)(i+(short)1);

			apdu.setOutgoingAndSend((short)0, le);
			break;
		
		case (byte) 0x50: // Test-APDU6 (CASE3)
			apdu.setIncomingAndReceive();
			apdu.setOutgoingAndSend((short) 0, (short) 0);
			break;

		case (byte) 0x55: // Test-APDU7 (waiting time extension + 9000)
			APDU.waitExtension();
			apdu.setOutgoingAndSend((short) 0, (short) 0);
			break;

		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
}
