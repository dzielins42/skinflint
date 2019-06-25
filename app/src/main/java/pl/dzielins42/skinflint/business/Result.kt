package pl.dzielins42.skinflint.business

class Result<T> private constructor(
    val data: T?,
    private val status: Status,
    val error: Throwable?
) {
    fun isDone(): Boolean = status == Status.DONE
    fun isError(): Boolean = status == Status.ERROR
    fun isInProgress(): Boolean = status == Status.IN_PROGRESS

    private enum class Status {
        ERROR, DONE, IN_PROGRESS
    }

    companion object {
        fun <T> error(data: T? = null, error: Throwable? = null): Result<T> {
            return Result(data, Status.ERROR, error)
        }

        fun <T> done(data: T? = null): Result<T> {
            return Result(data, Status.DONE, null)
        }

        fun <T> inProgress(data: T? = null): Result<T> {
            return Result(data, Status.DONE, null)
        }
    }
}