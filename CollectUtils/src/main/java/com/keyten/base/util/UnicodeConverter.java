package com.keyten.base.util;

import java.io.UnsupportedEncodingException;

/**
 * Unicode����ת����
 * @author Administrator
 *
 */
public class UnicodeConverter {



    private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
            'B', 'C', 'D', 'E', 'F' };
    private static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }

    /**

     * ���ַ�������� Unicode ��ʽ���ַ���. 
	 * ���磺
	 *     	 �վ�������У������ר��
	 *       ת����Ϊ��\u7A7A\u519B\u73B0\u5F79\u4E0A\u6821\u3001\u519B\u4E8B\u4E13\u5BB6
     * Converts unicodes to encoded \\uxxxx and escapes
     * special characters with a preceding slash
     * @param inputStr ��ת����Unicode������ַ�����
     * @param escapeSpace �Ƿ���Կո�Ϊtrueʱ�ڿո�����Ƿ�Ӹ���б�ܡ�
     * @return ����ת����Unicode������ַ�����
     */

    public static String encode(String inputStr, boolean escapeSpace) {
        int len = inputStr.length();
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuffer outBuffer = new StringBuffer(bufLen);
        for (int x = 0; x < len; x++) {
            char aChar = inputStr.charAt(x);
            // Handle common case first, selecting largest block that
            // avoids the specials below
            if ((aChar > 61) && (aChar < 127)) {
                if (aChar == '\\') {
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch (aChar) {
            case ' ':
                if (x == 0 || escapeSpace) outBuffer.append('\\');
                outBuffer.append(' ');
                break;
            case '\t':
                outBuffer.append('\\');
                outBuffer.append('t');
                break;
            case '\n':
                outBuffer.append('\\');
                outBuffer.append('n');
                break;
            case '\r':
                outBuffer.append('\\');
                outBuffer.append('r');
                break;
            case '\f':
                outBuffer.append('\\');
                outBuffer.append('f');
                break;
            case '=': // Fall through
            case ':': // Fall through
            case '#': // Fall through
            case '!':
                outBuffer.append('\\');
                outBuffer.append(aChar);
                break;
            default:
                if ((aChar < 0x0020) || (aChar > 0x007e)) {
                    // ÿ��unicode��16λ��ÿ��λ��Ӧ��16���ƴӸ�λ���浽��λ
                    outBuffer.append('\\');
                    outBuffer.append('u');
                    outBuffer.append(toHex((aChar >> 12) & 0xF));
                    outBuffer.append(toHex((aChar >> 8) & 0xF));
                    outBuffer.append(toHex((aChar >> 4) & 0xF));
                    outBuffer.append(toHex(aChar & 0xF));
                } else {
                    outBuffer.append(aChar);
                }
            }
        }
        return outBuffer.toString();
    }

    /**
     * �� Unicode ��ʽ���ַ���ת���ɶ�Ӧ�ı���������ַ����� 
	 * ���磺 \u7A7A\u519B\u73B0\u5F79\u4E0A\u6821\u3001\u519B\u4E8B\u4E13\u5BB6
	 *       ת����Ϊ���վ�������У������ר��
     * Converts encoded \\uxxxx to unicode chars
     * and changes special saved chars to their original forms
     * @param inputString  ��������ַ�����
     * @param off ת������ʼƫ������
     * @param convtBuf ת���Ļ����ַ����顣
     * @return ���ת�������ر���ǰ�������ַ�����
     */
    public static String decode(String inputString,int off) {
    	char[] in = inputString.toCharArray();
    	int len = inputString.length();
        char aChar;
        char[] out = new char[len]; // ֻ�̲���
        int outLen = 0;
        int end = off + len;
        while (off < end) {
            aChar = in[off++];
            if (aChar == '\\') {
                aChar = in[off++];
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = in[off++];
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }
                    out[outLen++] = (char) value;
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    out[outLen++] = aChar;
                }
            } else {
                out[outLen++] = (char) aChar;
            }
        }
        return new String(out, 0, outLen);
    }
    
    
    public static void main(String[] args) throws UnsupportedEncodingException 
    {
    	
        //String s = "�վ�������У������ר��";
        //String s="��־ǿ";
        //System.out.println("Original:" + s);
        //s = encode(s, true);
        //System.out.println("to unicode:" + s);
        //s = decode(s,0);
        //System.out.println("from unicode:" + s);
        
        
        
    	//String b="\u70ed\u95e8\u5fae\u535a";
    	//String b="\u4e3a\u4e86\u60a8\u7684\u5e10\u53f7\u5b89\u5168\uff0c\u8bf7\u8f93\u5165\u9a8c\u8bc1\u7801";
    	//b=decode(b,0);
    	//System.out.println(""+b);
    	//String str = decode("\u6309\u5e7f\u64ad\u65f6\u95f4",0);
    	//System.out.println(str);
    	
    	String str = "��ֻ�ǹ�Ա��ũ�嵽��ͷ�ʺ���ů";
    	str = UnicodeConverter.encode(str,true);
    	System.out.println(str);
    }
    
    
}
