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

public class TestApp_SW63C1_selectresponse_etsi extends TestApp_SWwarn_selectresponse_etsi
{
    public static void install(byte[] aArray, short sOffset, byte bLength)
    {
        new TestApp_SW63C1_selectresponse_etsi(aArray, sOffset, bLength);
    }

    private TestApp_SW63C1_selectresponse_etsi(byte[] aArray, short sOffset, byte bLength)
    {
	super((short)0x63C1);
        register(aArray, (short) (sOffset + 1), aArray[sOffset]);
    }
}
