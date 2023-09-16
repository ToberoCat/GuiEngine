package io.github.toberocat.guiengine.context

import io.github.toberocat.guiengine.components.GuiComponent

class RenderComputableFunctionProcessor {
    private val editComponents: MutableMap<String, MutableSet<Pair<String, String>>> = mutableMapOf()

    fun copy(renderComputableFunctionProcessor: RenderComputableFunctionProcessor) {
        editComponents.putAll(renderComputableFunctionProcessor.editComponents)
    }

    fun markAsComputed(
        component: GuiComponent,
        field: String,
        value: String
    ) {
        val list = editComponents.getOrDefault(component.id, mutableSetOf())
        list.add(field to value)
        editComponents[component.id] = list
    }

    fun editAll(context: GuiContext) {
        editComponents.forEach { (id, properties) ->
            context.editXmlComponentById(id) {
                for ((field, value) in properties)
                    it.fields[field] = context.api.xmlMapper.valueToTree(value)
            }
        }
    }
}