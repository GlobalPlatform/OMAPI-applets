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

public class TestApp_SW6999 extends Applet {

    public static void install(byte[] aArray, short sOffset, byte bLength) {
        new TestApp_SW6999(aArray, sOffset, bLength);
    }

    private TestApp_SW6999(byte[] aArray, short sOffset, byte bLength) {

        register(aArray, (short) (sOffset + 1), aArray[sOffset]);
    }

    public void process(APDU apdu) throws ISOException {

        if (selectingApplet()) {
            ISOException.throwIt(ISO7816.SW_APPLET_SELECT_FAILED);
        }

        ISOException.throwIt(ISO7816.SW_NO_ERROR);
    }
}
