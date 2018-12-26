package com.dbscarlet.common.dataResource

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.atomic.AtomicReference

/**
 * Created by Daibing Wang on 2018/12/6.
 */
 
fun <T> Publisher<T>.toResLiveData(): ResLiveData<T> {
    return PublisherResLiveData(this)
}

private class PublisherResLiveData<T>(private val mPublisher: Publisher<T>) : ResLiveData<T>() {
    private val mSubscriber: AtomicReference<LiveDataSubscriber> = AtomicReference()

    override fun onActive() {
        super.onActive()
        val s = LiveDataSubscriber()
        mSubscriber.set(s)
        mPublisher.subscribe(s)
    }

    override fun onInactive() {
        super.onInactive()
        val s = mSubscriber.getAndSet(null)
        s?.cancelSubscription()
    }

    internal inner class LiveDataSubscriber : AtomicReference<Subscription>(), Subscriber<T> {

        override fun onSubscribe(s: Subscription) {
            if (compareAndSet(null, s)) {
                postValue(Loading())
                s.request(java.lang.Long.MAX_VALUE)
            } else {
                s.cancel()
            }
        }

        override fun onNext(item: T) {
            postValue(Success(item))
        }

        override fun onError(ex: Throwable) {
            mSubscriber.compareAndSet(this, null)
            postValue(Error(cause = ex))
        }

        override fun onComplete() {
            mSubscriber.compareAndSet(this, null)
        }

        fun cancelSubscription() {
            val s = get()
            s?.cancel()
            postValue(Cancel())
        }
    }
}