package nandcat.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Object cloner. Used for deep-cloning Objects in a fast way using fast byte streams.
 */
public final class FastDeepCopy {

    /**
     * Private constructor.
     */
    private FastDeepCopy() {
    }

    /**
     * Returns a deep copy of the object, or null if the object cannot be serialized.
     * 
     * @param orig
     *            Original object to clone with all attributes.
     * @return Cloned object or null if object cannot be serialized.
     */
    public static Object copy(Serializable orig) {
        Object obj = null;
        try {
            // Write the object out to a byte array
            FastByteArrayOutputStream fbos = new FastDeepCopy().new FastByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(fbos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Retrieve an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(fbos.getInputStream());
            obj = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }

    /**
     * InputStream based on a ByteArray.
     */
    private class FastByteArrayInputStream extends InputStream {

        /**
         * Byte buffer.
         */
        private byte[] buf = null;

        /**
         * Current size of the buffer.
         */
        private int count = 0;

        /**
         * Number of bytes that have been read from the buffer.
         */
        private int pos = 0;

        /**
         * Constructs a new FastByteArrayInputStream with a buffer and a buffer size.
         * 
         * @param buf
         *            Buffer to use.
         * @param count
         *            Size of the buffer.
         */
        public FastByteArrayInputStream(byte[] buf, int count) {
            this.buf = buf;
            this.count = count;
        }

        /**
         * Checks if space is available to write to.
         * 
         * @return Byte available.
         */
        public final int available() {
            return count - pos;
        }

        /**
         * Reads a byte from the buffer and returns a Integer representation if possible and increases the cursor.
         * 
         * @return Integer representation of read byte, if not possible -1.
         */
        public final int read() {
            return (pos < count) ? (buf[pos++] & 0xff) : -1;
        }

        /**
         * Reads an amount (len) of byte from buffer and writes them to given ByteArray at starting position (off).
         * 
         * @param b
         *            Write read data to.
         * @param off
         *            Offset to start writing at given b.
         * @param len
         *            Length of bytes to read.
         * @return Read length, -1 iff read not possible.
         */
        public final int read(byte[] b, int off, int len) {
            if (pos >= count) {
                return -1;
            }

            if ((pos + len) > count) {
                len = (count - pos);
            }

            System.arraycopy(buf, pos, b, off, len);
            pos += len;
            return len;
        }

        /**
         * Skips n Bytes.
         * 
         * @param n
         *            Amount of bytes to skip.
         * @return New position of cursor.
         */
        public final long skip(long n) {
            if ((pos + n) > count) {
                n = count - pos;
            }
            if (n < 0) {
                return 0;
            }
            pos += n;
            return n;
        }
    }

    /**
     * FastByteArrayOutputStream. OutputStream working with a byte array.
     */
    private class FastByteArrayOutputStream extends OutputStream {

        /**
         * Buffer.
         */
        private byte[] buf = null;

        /**
         * Used size, a cursor.
         */
        private int size = 0;

        /**
         * Default initial buffer size 5k.
         */
        private static final int INITIAL_BUFFER_SIZE = 5 * 1024;

        /**
         * Constructs a stream with default buffer capacity.
         */
        public FastByteArrayOutputStream() {
            this(INITIAL_BUFFER_SIZE);
        }

        /**
         * Constructs a stream with the given initial size.
         * 
         * @param initSize
         *            Size to init buffer with;
         */
        public FastByteArrayOutputStream(int initSize) {
            this.size = 0;
            this.buf = new byte[initSize];
        }

        /**
         * Ensures that we have a large enough buffer for the given size. If needed expands the buffer.
         * 
         * @param size
         *            Size to verify buffer is large enough.
         */
        private void verifyBufferSize(int size) {
            if (size > buf.length) {
                byte[] old = buf;
                buf = new byte[Math.max(size, 2 * buf.length)];
                System.arraycopy(old, 0, buf, 0, old.length);
                old = null;
            }
        }

        /**
         * Appends content of given ByteArray to buffer.
         * 
         * @param b
         *            ByteArray to append.
         */
        public final void write(byte[] b) {
            verifyBufferSize(size + b.length);
            System.arraycopy(b, 0, buf, size, b.length);
            size += b.length;
        }

        /**
         * Appends content of given ByteArray to buffer from given offset and length.
         * 
         * @param b
         *            ByteArray to append.
         * @param off
         *            Offset of given array.
         * @param len
         *            Length of array to copy.
         */
        public final void write(byte[] b, int off, int len) {
            verifyBufferSize(size + len);
            System.arraycopy(b, off, buf, size, len);
            size += len;
        }

        /**
         * Appends byte to buffer.
         * 
         * @param b
         *            Integer as Byte to append.
         */
        public final void write(int b) {
            verifyBufferSize(size + 1);
            buf[size++] = (byte) b;
        }

        /**
         * Returns a ByteArrayInputStream for reading back the written data.
         * 
         * @return InputStream.
         */
        public InputStream getInputStream() {
            return new FastByteArrayInputStream(buf, size);
        }
    }
}
