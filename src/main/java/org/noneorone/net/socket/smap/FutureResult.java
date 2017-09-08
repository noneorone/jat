package org.noneorone.net.socket.smap;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.lang.reflect.InvocationTargetException;

public class FutureResult
{

  // Fields
  protected Object value_;
  protected boolean ready_;
  protected InvocationTargetException exception_;

  // Constructors
  public FutureResult()
  {}

  // Methods

  protected Object doGet()
      throws InvocationTargetException
  {
    return null;
  }

  public synchronized Object get()
      throws InterruptedException, InvocationTargetException
  {
    return null;
  }

  public synchronized Object timedGet(long msecs)
      throws InterruptedException, InvocationTargetException
  {
    return null;
  }

  public synchronized void set(Object newValue)
  {}

  public synchronized void setException(Throwable ex)
  {}

  public synchronized InvocationTargetException getException()
  {
    return null;
  }

  public synchronized boolean isReady()
  {
    return false;
  }

  public synchronized Object peek()
  {
    return null;
  }

  public synchronized void clear()
  {}
}
