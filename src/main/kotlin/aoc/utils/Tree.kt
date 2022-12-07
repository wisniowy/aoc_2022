package aoc.utils

class Tree<T> (private val root: TreeNode<T>){

    fun getRoot(): TreeNode<T> { return root }

    class TreeNode<T>(val value: T) {

        constructor(value: T, parent: TreeNode<T>) : this(value) {
            this.parent = parent
        }

        var parent: TreeNode<T>? = null
        val children = mutableListOf<TreeNode<T>>()

        fun add(child: TreeNode<T>) {
            children.add(child)
        }

        fun <S> forEachDepthFirst(visit: (TreeNode<T>) -> S, agg: (S, S) -> S): S {
            var result = visit(this)
            val childrenResult = children.map{ it.forEachDepthFirst(visit, agg) }
            result = if (childrenResult.isNotEmpty()) agg(childrenResult.reduce(agg), result)
                        else result
            return result
        }
    }
}

