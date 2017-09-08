package org.noneorone.net.socket.smap;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author tongxianwu
 * @version 1.0
 */

public class LinkedQueue
{

  protected LinkedNode head_;
  protected final Object putLock_ = new Object();
  protected LinkedNode last_;
  protected int waitingForTake_;

  public LinkedQueue()
  {
    waitingForTake_ = 0;
    head_ = new LinkedNode(null);
    last_ = head_;
  }

  protected void insert(Object x)
  {
    synchronized (putLock_)
    {
      LinkedNode p = new LinkedNode(x);
      synchronized (last_)
      {
        last_.next = p;
        last_ = p;
      }
      if (waitingForTake_ > 0)
      {
        putLock_.notify();
      }
    }
  }

  protected synchronized Object extract()
  {
    Object obj;
    synchronized (head_)
    {
      Object x = null;
      LinkedNode first = head_.next;
      if (first != null)
      {
        x = first.value;
        first.value = null;
        head_ = first;
      }
      obj = x;
    }
    return obj;
  }

  public void put(Object x)
      throws InterruptedException
  {
    if (x == null)
    {
      throw new IllegalArgumentException();
    }
    if (Thread.interrupted())
    {
      throw new InterruptedException();
    }
    else
    {
      insert(x);
      return;
    }
  }

  public boolean offer(Object x, long msecs)
      throws InterruptedException
  {
    if (x == null)
    {
      throw new IllegalArgumentException();
    }
    if (Thread.interrupted())
    {
      throw new InterruptedException();
    }
    else
    {
      insert(x);
      return true;
    }
  }

  public Object take()
      throws InterruptedException
  {
    if (Thread.interrupted())
    {
      throw new InterruptedException();
    }
    Object x = extract();
    if (x != null)
    {
      return x;
    }
 synchronized (putLock_){
    try
    {
      waitingForTake_++;
      do
      {
        x = extract();
        if (x != null)
        {
          waitingForTake_--;
          Object obj1 = x;
          return obj1;
        }
        putLock_.wait();
      }
      while (true);
    }
    catch (InterruptedException ex)
    {
      waitingForTake_--;
      putLock_.notify();
      throw ex;
    }
 }
  }

  public Object peek()
  {
    Object obj1;
    synchronized (head_)
    {
      LinkedNode first = head_.next;
      if (first != null)
      {
        Object obj = first.value;
        return obj;
      }
      obj1 = null;
    }
    return obj1;
  }

  public boolean isEmpty()
  {
    boolean flag;
    synchronized (head_)
    {
      flag = head_.next == null;
    }
    return flag;
  }

  public Object poll(long msecs)
      throws InterruptedException
  {
    if (Thread.interrupted())
    {
      throw new InterruptedException();
    }
    Object x = extract();
    if (x != null)
    {
      return x;
    }

    try
    {
      long waitTime = msecs;
      long start = msecs > 0L ? System.currentTimeMillis() : 0L;
      synchronized (putLock_){
        waitingForTake_++;
        do
        {
          x = extract();
          if (x != null || waitTime <= 0L)
          {
            waitingForTake_--;
            Object obj1 = x;
            return obj1;
          }
          putLock_.wait(waitTime);
          waitTime = msecs - (System.currentTimeMillis() - start);
        }
        while (true);
      }
    }
    catch (InterruptedException ex)
    {
      waitingForTake_--;
      putLock_.notify();
      throw ex;
    }

  }

  class LinkedNode
  {

    public Object value;
    public LinkedNode next;

    public LinkedNode()
    {
    }

    public LinkedNode(Object x)
    {
      value = x;
    }

    public LinkedNode(Object x, LinkedNode n)
    {
      value = x;
      next = n;
    }
  }

}
