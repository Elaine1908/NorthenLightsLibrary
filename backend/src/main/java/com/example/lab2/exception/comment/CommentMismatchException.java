package com.example.lab2.exception.comment;

/**
 * 用户删除评论时，该评论的发布者与删除的用户不是同一个人
 * 抛出的异常
 */
public class CommentMismatchException extends RuntimeException  {
    public CommentMismatchException(String s){super(s);}
}
