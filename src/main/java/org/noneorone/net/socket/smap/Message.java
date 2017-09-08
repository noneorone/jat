package org.noneorone.net.socket.smap;

// $Id: Message.java,v 1.53 2006/08/13 15:38:52 belaban Exp $





import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;


/**
 * A Message encapsulates data sent to members of a group. It contains among other things the
 * address of the sender, the destination address, a payload (byte buffer) and a list of
 * headers. Headers are added by protocols on the sender side and removed by protocols
 * on the receiver's side.
 * <p>
 * The byte buffer can point to a reference, and we can subset it using index and length. However,
 * when the message is serialized, we only write the bytes between index and length.
 * @author Bela Ban
 */
public class Message {
    protected IpAddress dest_addr=null;
    protected IpAddress src_addr=null;

    /** The payload */
    private byte[]    buf=null;

    /** The index into the payload (usually 0) */
    protected transient int     offset=0;

    /** The number of bytes in the buffer (usually buf.length is buf not equal to null). */
    protected transient int     length=0;

    /** Map<String,Header> */
    protected Map headers;

    protected static final Log log=LogFactory.getLog(Message.class);



    /** Map<Address,Address>. Maintains mappings to canonical addresses */
  //  private static final Map canonicalAddresses=new ConcurrentReaderHashMap();


    static {
        boolean b;
        try {
            b=Boolean.getBoolean("disable_canonicalization");
        }
        catch (java.security.AccessControlException e) {
            // this will happen in an applet context
            b=false;
        }

    }


    /** Public constructor
     *  @param dest Address of receiver. If it is <em>null</em> or a <em>string</em>, then
     *              it is sent to the group (either to current group or to the group as given
     *              in the string). If it is a Vector, then it contains a number of addresses
     *              to which it must be sent. Otherwise, it contains a single destination.<p>
     *              Addresses are generally untyped (all are of type <em>Object</em>. A channel
     *              instance must know what types of addresses it expects and downcast
     *              accordingly.
     */
    public Message(IpAddress dest) {
        dest_addr=dest;

    }

    /** Public constructor
     *  @param dest Address of receiver. If it is <em>null</em> or a <em>string</em>, then
     *              it is sent to the group (either to current group or to the group as given
     *              in the string). If it is a Vector, then it contains a number of addresses
     *              to which it must be sent. Otherwise, it contains a single destination.<p>
     *              Addresses are generally untyped (all are of type <em>Object</em>. A channel
     *              instance must know what types of addresses it expects and downcast
     *              accordingly.
     *  @param src  Address of sender
     *  @param buf  Message to be sent. Note that this buffer must not be modified (e.g. buf[0]=0 is
     *              not allowed), since we don't copy the contents on clopy() or clone().
     */
    public Message(IpAddress dest, IpAddress src, byte[] buf) {
        this(dest);
        src_addr=src;
        setBuffer(buf);
    }

    /**
     * Constructs a message. The index and length parameters allow to provide a <em>reference</em> to
     * a byte buffer, rather than a copy, and refer to a subset of the buffer. This is important when
     * we want to avoid copying. When the message is serialized, only the subset is serialized.
     * @param dest Address of receiver. If it is <em>null</em> or a <em>string</em>, then
     *              it is sent to the group (either to current group or to the group as given
     *              in the string). If it is a Vector, then it contains a number of addresses
     *              to which it must be sent. Otherwise, it contains a single destination.<p>
     *              Addresses are generally untyped (all are of type <em>Object</em>. A channel
     *              instance must know what types of addresses it expects and downcast
     *              accordingly.
     * @param src    Address of sender
     * @param buf    A reference to a byte buffer
     * @param offset The index into the byte buffer
     * @param length The number of bytes to be used from <tt>buf</tt>. Both index and length are checked for
     *               array index violations and an ArrayIndexOutOfBoundsException will be thrown if invalid
     */
    public Message(IpAddress dest, IpAddress src, byte[] buf, int offset, int length) {
        this(dest);
        src_addr=src;
        setBuffer(buf, offset, length);
    }


    /** Public constructor
     *  @param dest Address of receiver. If it is <em>null</em> or a <em>string</em>, then
     *              it is sent to the group (either to current group or to the group as given
     *              in the string). If it is a Vector, then it contains a number of addresses
     *              to which it must be sent. Otherwise, it contains a single destination.<p>
     *              Addresses are generally untyped (all are of type <em>Object</em>. A channel
     *              instance must know what types of addresses it expects and downcast
     *              accordingly.
     *  @param src  Address of sender
     *  @param obj  The object will be serialized into the byte buffer. <em>Object
     *              has to be serializable </em>! Note that the resulting buffer must not be modified
     *              (e.g. buf[0]=0 is not allowed), since we don't copy the contents on clopy() or clone().
     */


    public Message() {

    }




    public IpAddress getDest() {
        return dest_addr;
    }

    public void setDest(IpAddress new_dest) {

            dest_addr=new_dest;

    }

    public IpAddress getSrc() {
        return src_addr;
    }

    public void setSrc(IpAddress new_src) {

            src_addr=new_src;

    }

    /**
     * Returns a <em>reference</em> to the payload (byte buffer). Note that this buffer should not be modified as
     * we do not copy the buffer on copy() or clone(): the buffer of the copied message is simply a reference to
     * the old buffer.<br/>
     * Even if offset and length are used: we return the <em>entire</em> buffer, not a subset.
     */
    public byte[] getRawBuffer() {
        return buf;
    }

    /**
     * Returns a copy of the buffer if offset and length are used, otherwise a reference.
     * @return byte array with a copy of the buffer.
     */
    final public byte[] getBuffer() {
        if(buf == null)
            return null;
        if(offset == 0 && length == buf.length)
            return buf;
        else {
            byte[] retval=new byte[length];
            System.arraycopy(buf, offset, retval, 0, length);
            return retval;
        }
    }

    final public void setBuffer(byte[] b) {
        buf=b;
        if(buf != null) {
            offset=0;
            length=buf.length;
        }
        else {
            offset=length=0;
        }
    }

    /**
     * Set the internal buffer to point to a subset of a given buffer
     * @param b The reference to a given buffer. If null, we'll reset the buffer to null
     * @param offset The initial position
     * @param length The number of bytes
     */
    final public void setBuffer(byte[] b, int offset, int length) {
        buf=b;
        if(buf != null) {
            if(offset < 0 || offset > buf.length)
                throw new ArrayIndexOutOfBoundsException(offset);
            if((offset + length) > buf.length)
                throw new ArrayIndexOutOfBoundsException((offset+length));
            this.offset=offset;
            this.length=length;
        }
        else {
            offset=length=0;
        }
    }

    /** Returns the offset into the buffer at which the data starts */
    public int getOffset() {
        return offset;
    }

    /** Returns the number of bytes in the buffer */
    public int getLength() {
        return length;
    }

    public Map getHeaders() {
        return headers;
    }




    /**
     * Nulls all fields of this message so that the message can be reused. Removes all headers from the
     * hashmap, but keeps the hashmap
     */
    public void reset() {
        dest_addr=src_addr=null;
        setBuffer(null);

    }

    /*---------------------- Used by protocol layers ----------------------*/

    /** Puts a header given a key into the hashmap. Overwrites potential existing entry. */



    public Message copy() {
        return copy(true);
    }

    /**
     * Create a copy of the message. If offset and length are used (to refer to another buffer), the copy will
     * contain only the subset offset and length point to, copying the subset into the new copy.
     * @param copy_buffer
     * @return Message with specified data
     */
    public Message copy(boolean copy_buffer) {
        Message retval=new Message();
        retval.dest_addr=dest_addr;
        retval.src_addr=src_addr;

        if(copy_buffer && buf != null) {

            // change bela Feb 26 2004: we don't resolve the reference
            retval.setBuffer(buf, offset, length);
        }


        return retval;
    }


    protected Object clone() throws CloneNotSupportedException {
        return copy();
    }

    public Message makeReply() {
        return new Message(src_addr);
    }


    public String toString() {
        StringBuffer ret=new StringBuffer(64);
        ret.append("[dst: ");
        if(dest_addr == null)
            ret.append("<null>");
        else
            ret.append(dest_addr);
        ret.append(", src: ");
        if(src_addr == null)
            ret.append("<null>");
        else
            ret.append(src_addr);

        int size;
        if(headers != null && (size=headers.size()) > 0)
            ret.append(" (").append(size).append(" headers)");

        ret.append(", size = ");
        if(buf != null && length > 0)
            ret.append(length);
        else
            ret.append('0');
        ret.append(" bytes");
        ret.append(']');
        return ret.toString();
    }


    /** Tries to read an object from the message's buffer and prints it */



    /**
     * Returns size of buffer, plus some constant overhead for src and dest, plus number of headers time
     * some estimated size/header. The latter is needed because we don't want to marshal all headers just
     * to find out their size requirements. If a header implements Sizeable, the we can get the correct
     * size.<p> Size estimations don't have to be very accurate since this is mainly used by FRAG to
     * determine whether to fragment a message or not. Fragmentation will then serialize the message,
     * therefore getting the correct value.
     */





    public String printObjectHeaders() {
        StringBuffer sb=new StringBuffer();
        Map.Entry entry;

        if(headers != null) {
            for(Iterator it=headers.entrySet().iterator(); it.hasNext();) {
                entry=(Map.Entry)it.next();
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append('\n');
            }
        }
        return sb.toString();
    }



    /* ----------------------------------- Interface Externalizable ------------------------------- */



    /* --------------------------------- End of Interface Externalizable ----------------------------- */





}
