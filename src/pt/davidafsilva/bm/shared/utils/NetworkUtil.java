package pt.davidafsilva.bm.shared.utils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 * NetworkUtil.java
 * 
 * This class provides two mechanisms to read and write from/to a stream, respectively.
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 9:45:33 PM
 */
public class NetworkUtil {
	
	/**
	 * Writes the given object to the object output stream
	 * 
	 * @param clazz
	 * 		The class of the object to write
	 * @param obj
	 * 		The object to write
	 * @param out
	 * 		The output stream 
	 * @throws IOExceptions
	 */
	public static <T> void write(Class<T> clazz, T obj, ObjectOutput out) throws IOException {
		if (out == null)
			return;
		
		//boolean
		if (clazz == boolean.class || clazz == Boolean.class) {
			out.writeBoolean((Boolean) obj);
		} else if (clazz == Boolean.class) {
			out.writeByte(obj == null ? 0x00 : 0x01);
			if (obj != null)
				out.writeBoolean((Boolean) obj);
		}
		
		//byte
		else if (clazz == byte.class) {
			out.writeByte((Byte) obj);
		} else if (clazz == Byte.class) {
			out.writeByte(obj == null ? 0x00 : 0x01);
			if (obj != null)
				out.writeByte((Byte) obj);
		}
		
		//short
		else if (clazz == short.class) {
			out.writeShort((Short) obj);
		} else if (clazz == Short.class) {
			out.writeByte(obj == null ? 0x00 : 0x01);
			if (obj != null)
				out.writeShort((Short) obj);
		}
		
		//integer
		else if (clazz == int.class) {
			out.writeInt((Integer) obj);
		} else if (clazz == Integer.class) {
			out.writeByte(obj == null ? 0x00 : 0x01);
			if (obj != null)
				out.writeInt((Integer) obj);
		}
		
		//double
		else if (clazz == double.class) {
			out.writeDouble((Double) obj);
		} else if (clazz == Double.class) {
			out.writeByte(obj == null ? 0x00 : 0x01);
			if (obj != null)
				out.writeDouble((Double) obj);
		}
		
		//float
		else if (clazz == float.class) {
			out.writeFloat((Float) obj);
		} else if (clazz == Float.class) {
			out.writeByte(obj == null ? 0x00 : 0x01);
			if (obj != null)
				out.writeFloat((Float) obj);
		}
		
		//long
		else if (clazz == long.class) {
			out.writeLong((Long) obj);
		} else if (clazz == Long.class) {
			out.writeByte(obj == null ? 0x00 : 0x01);
			if (obj != null)
				out.writeLong((Long) obj);
		}
		
		//char
		else if (clazz == char.class) {
			out.writeChar((Character) obj);
		} else if (clazz == Character.class) {
			out.writeByte(obj == null ? 0x00 : 0x01);
			if (obj != null)
				out.writeChar((Character) obj);
		}
		
		//string
		else if (clazz == String.class) {
			out.writeInt(obj == null ? -1 : obj.toString().length());
			if (obj != null) {
				for (byte b : obj.toString().getBytes("UTF-8"))
					out.writeByte(b);
			}
		}
		
		// Externalizable classes
		else if (containsInterface(clazz.getInterfaces(), Externalizable.class)) {
			((Externalizable) obj).writeExternal(out);
		}
	}
	
	/**
	 * Reads the given class type from the object input stream.
	 * 
	 * @param clazz
	 * 			The class of the object to read
	 * @param in
	 * 			The input stream
	 * @return
	 * 			The object read
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T read(Class<T> clazz, ObjectInput in) throws IOException, ClassNotFoundException {
		if (in == null)
			return null;
		
		boolean exists = false;
		int size;
		
		//boolean
		if (clazz == boolean.class) {
			return (T) (Boolean) in.readBoolean();
		} else if (clazz == Boolean.class) {
			exists = in.readByte() == 0x01;
			return exists ? (T) (Boolean) in.readBoolean() : null;
		}
		
		//byte
		else if (clazz == byte.class) {
			return (T) (Byte) in.readByte();
		} else if (clazz == Byte.class) {
			exists = in.readByte() == 0x01;
			return exists ? (T) (Byte) in.readByte() : null;
		}
		
		//short
		else if (clazz == short.class) {
			return (T) (Short) in.readShort();
		} else if (clazz == Short.class) {
			exists = in.readByte() == 0x01;
			return exists ? (T) (Short) in.readShort() : null;
		}
		
		//integer
		else if (clazz == int.class) {
			return (T) (Integer) in.readInt();
		} else if (clazz == Integer.class) {
			exists = in.readByte() == 0x01;
			return exists ? (T) (Integer) in.readInt() : null;
		}
		
		//double
		else if (clazz == double.class) {
			return (T) (Double) in.readDouble();
		} else if (clazz == Double.class) {
			exists = in.readByte() == 0x01;
			return exists ? (T) (Double) in.readDouble() : null;
		}
		
		//float
		else if (clazz == float.class) {
			return (T) (Float) in.readFloat();
		} else if (clazz == Float.class) {
			exists = in.readByte() == 0x01;
			return exists ? (T) (Float) in.readFloat() : null;
		}
		
		//long
		else if (clazz == long.class) {
			return (T) (Long) in.readLong();
		} else if (clazz == Long.class) {
			exists = in.readByte() == 0x01;
			return exists ? (T) (Long) in.readLong() : null;
		}
		
		//char
		else if (clazz == char.class) {
			return (T) (Character) in.readChar();
		} else if (clazz == Character.class) {
			exists = in.readByte() == 0x01;
			return exists ? (T) (Character) in.readChar() : null;
		}
		
		//string
		else if (clazz == String.class) {
			size = in.readInt();
			if (size == -1)
				return null;
			
			byte[] str = new byte[size];
			for (int i = 0; i < size; i++) {
				str[i] = in.readByte();
			}
			return (T) new String(str, "UTF-8");
		}
		
		// Externalizable classes
		else if (containsInterface(clazz.getInterfaces(), Externalizable.class)) {
			try {
				T instance = clazz.newInstance();
				((Externalizable) instance).readExternal(in);
				return instance;
			} catch (InstantiationException | IllegalAccessException e) {
				return null;
			}
		}
		
		return null;
	}
	
	/**
	 * Checks if a given interface exists in the given array of interfaces
	 * 
	 * @param interfaces
	 * 		The interfaces array
	 * @param iface
	 * 		The interface to look for
	 * @return
	 * 		<code>true</code> if <code>iface</code> exists in the <code>interfaces</code> array,
	 * 		<code>false</code> otherwise.
	 */
	private static boolean containsInterface(Class<?>[] interfaces, Class<?> iface) {
		for (int i = 0; i < interfaces.length; i++)
			if (interfaces[i] == iface)
				return true;
		
		return false;
		
	}
}
