package hu.unideb.inf.segfaulter.main;

import java.lang.reflect.Field;
import java.util.Random;

import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public class Main {
  private static final String UNSAFE_FIELD_NAME = "theUnsafe";

  public static void main(String... args) throws Exception {
    Unsafe unsafe = getUnsafe();
    long addressOf = addressOf(unsafe, Object.class);
    unsafe.putInt(addressOf, new Random().nextInt());
  }

  private static Unsafe getUnsafe() throws Exception {
    Field field = sun.misc.Unsafe.class.getDeclaredField(UNSAFE_FIELD_NAME);
    field.setAccessible(true);
    return (sun.misc.Unsafe) field.get(null);
  }

  public static long addressOf(Unsafe unsafe, Object o)
      throws Exception {
    Object[] array = new Object[] { o };

    long baseOffset = unsafe.arrayBaseOffset(Object[].class);
    int addressSize = unsafe.addressSize();
    long objectAddress;
    switch (addressSize) {
      case 4:
        objectAddress = unsafe.getInt(array, baseOffset);
        break;
      case 8:
        objectAddress = unsafe.getLong(array, baseOffset);
        break;
      default:
        throw new Error("unsupported address size: " + addressSize);
    }

    return (objectAddress);
  }
}
