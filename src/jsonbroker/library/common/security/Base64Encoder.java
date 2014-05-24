
// vvv derived from http://www.source-code.biz/base64coder/java/Base64Coder.java.txt

// Copyright 2003-2010 Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland
// www.source-code.biz, www.inventec.ch/chdh
//
// This module is multi-licensed and may be used under the terms
// of any of the following licenses:
//
//  EPL, Eclipse Public License, V1.0 or later, http://www.eclipse.org/legal
//  LGPL, GNU Lesser General Public License, V2.1 or later, http://www.gnu.org/licenses/lgpl.html
//  GPL, GNU General Public License, V2 or later, http://www.gnu.org/licenses/gpl.html
//  AL, Apache License, V2.0 or later, http://www.apache.org/licenses
//  BSD, BSD License, http://www.opensource.org/licenses/bsd-license.php
//  MIT, MIT License, http://www.opensource.org/licenses/MIT
//
// Please contact the author if you need another license.
// This module is provided "as is", without warranties of any kind.


package jsonbroker.library.common.security;


/**
* A Base64 encoder/decoder.
*
* <p>
* This class is used to encode and decode data in Base64 format as described in RFC 1521.
*
* <p>
* Project home page: <a href="http://www.source-code.biz/base64coder/java/">www.source-code.biz/base64coder/java</a><br>
* Author: Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland<br>
* Multi-licensed: EPL / LGPL / GPL / AL / BSD / MIT.
*/
class Base64Encoder {


// Mapping table from 6-bit nibbles to Base64 characters.
private static final char[] map1 = new char[64];
   static {
      int i=0;
      for (char c='A'; c<='Z'; c++) map1[i++] = c;
      for (char c='a'; c<='z'; c++) map1[i++] = c;
      for (char c='0'; c<='9'; c++) map1[i++] = c;
      map1[i++] = '+'; map1[i++] = '/'; }

// Mapping table from Base64 characters to 6-bit nibbles.
private static final byte[] map2 = new byte[128];
   static {
      for (int i=0; i<map2.length; i++) map2[i] = -1;
      for (int i=0; i<64; i++) map2[map1[i]] = (byte)i; }



/**
* Encodes a byte array into Base64 format.
* No blanks or line breaks are inserted in the output.
* @param in  An array containing the data bytes to be encoded.
* @return    A character array containing the Base64 encoded data.
*/
static char[] encode (byte[] in) {
   return encode(in, 0, in.length); }

/**
* Encodes a byte array into Base64 format.
* No blanks or line breaks are inserted in the output.
* @param in    An array containing the data bytes to be encoded.
* @param iLen  Number of bytes to process in <code>in</code>.
* @return      A character array containing the Base64 encoded data.
*/
static char[] encode (byte[] in, int iLen) {
   return encode(in, 0, iLen); }

/**
* Encodes a byte array into Base64 format.
* No blanks or line breaks are inserted in the output.
* @param in    An array containing the data bytes to be encoded.
* @param iOff  Offset of the first byte in <code>in</code> to be processed.
* @param iLen  Number of bytes to process in <code>in</code>, starting at <code>iOff</code>.
* @return      A character array containing the Base64 encoded data.
*/
static char[] encode (byte[] in, int iOff, int iLen) {
   int oDataLen = (iLen*4+2)/3;       // output length without padding
   int oLen = ((iLen+2)/3)*4;         // output length including padding
   char[] out = new char[oLen];
   int ip = iOff;
   int iEnd = iOff + iLen;
   int op = 0;
   while (ip < iEnd) {
      int i0 = in[ip++] & 0xff;
      int i1 = ip < iEnd ? in[ip++] & 0xff : 0;
      int i2 = ip < iEnd ? in[ip++] & 0xff : 0;
      int o0 = i0 >>> 2;
      int o1 = ((i0 &   3) << 4) | (i1 >>> 4);
      int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
      int o3 = i2 & 0x3F;
      out[op++] = map1[o0];
      out[op++] = map1[o1];
      out[op] = op < oDataLen ? map1[o2] : '='; op++;
      out[op] = op < oDataLen ? map1[o3] : '='; op++; }
   return out; }


// Dummy constructor.
private Base64Encoder() {}

} // end class Base64Coder

// ^^^ derived from http://www.source-code.biz/base64coder/java/Base64Coder.java.txt
