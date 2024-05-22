package jp.co.rakuten.htmxpoc

class TemplateUtils {

    companion object {

        fun createEle(child:String, attr:String, tag:String = "div"): String {
            return  "<$tag $attr>$child</$tag>"
        }

        fun container(child:String, attr:String):String {
            return createEle(child, attr)
        }

        fun todosContainer (
            child:String = "<span style=\"margin-left:.5rem\" class=\"md-typescale-body-medium\" id=\"no-items\"> No items </span>"
        ):String {
            return container(child, "hx-swap-oob=\"innerHTML:#todos-list\"")
        }

        fun renderTodos(list: List<Todo>, listItemView:String):String {
            try {
                var todosTemplate = ""

                for (todo in list) {
                    todosTemplate += "" +
                            HtmlParser.parseView(listItemView, todo) +
                            ""
                }

                if (todosTemplate !== "") {
                    return todosContainer(todosTemplate);
                }
                return todosContainer();
            } catch (error:Exception) {
                println("Exception on renderTodos: ${error.message}");
                return todosContainer();
            }
        }

        fun renderTodo(list: List<Todo>, listItemView:String):String {
            return try {
                HtmlParser.parseView(listItemView, list)
            } catch (error:Exception) {
                println("Exception on renderTodo: ${error.message}")
                todosContainer()
            }
        }
    }
}
