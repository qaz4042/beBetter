package bebetter.mybatisplus;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.TimerTask;

/**
 * @author LiZuBin
 * @date 2020/8/20
 */
public class Transaction {

    /**
     * 如果有启事务,在事务提交后执行(避免下一步查不到上一步入库还没提交数据)
     *
     * @param runnable 执行内容,常常是消息推送等异步操作
     */
    public static void ifTransactionDoAfter(Runnable runnable) {
        ifTransactionDoAfter(runnable, true);
    }

    /**
     * 如果有启事务,在事务提交后执行(避免下一步查不到上一步入库还没提交数据)
     *
     * @param runnable  执行内容,常常是消息推送等异步操作
     * @param newThread 新线程(如果报错)不会影响主业务返回成功
     */
    public static void ifTransactionDoAfter(Runnable runnable, Boolean newThread) {
        //如果在事务里
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            //提交后执行
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    if (newThread) {
                        new Thread(new TimerTask() {
                            @Override
                            public void run() {
                                runnable.run();
                            }
                        }).start();
                    } else {
                        runnable.run();
                    }
                }
            });
        } else {
            //直接执行
            runnable.run();
        }
    }
}
