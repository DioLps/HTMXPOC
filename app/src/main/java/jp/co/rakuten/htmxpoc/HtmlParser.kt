package jp.co.rakuten.htmxpoc

class HtmlParser {
    companion object {
        fun parseView(view: String, data: Any): String {
            try {
                val tokenKeys = (data as Map<String,Any>).keys.toList();

                var render = ""

                if (view.isNotBlank() && view.isNotEmpty()) {
                    for (key in tokenKeys) {
                        val item = data[key];
                        if (item is Boolean) {
                            render = view.replace("{{${key}}}", item.toString())
                        } else if (item != null) {
                            // TODO: check which one to use when dealing with checkboxes
                            //  view = view.replaceAll(`{{${key}}}`, "checked");
                            render = view.replace("{{${key}}}", "checked");
                        }
                    }

                }

                return render
            } catch (error: Exception) {
                println("Exception on parseView: ${error.message}")
                return "Error rendering!"
            }
        }

        fun parseAndLoad(view: String, data: Map<String, Any>): String {
            return try {
                parseView(view, data);
            } catch (error: Exception) {
                println("Exception on parseAndLoad: ${error.message}")
                "Error rendering!"
            }
        }
    }
}