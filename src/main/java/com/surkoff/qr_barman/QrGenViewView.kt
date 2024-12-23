package com.surkoff.qr_barman

import com.vaadin.flow.component.Composite
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Menu
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import com.vaadin.flow.spring.annotation.UIScope
import com.vaadin.flow.theme.lumo.LumoUtility
import org.springframework.beans.factory.annotation.Autowired

private const val WIDTH_100_PERCENT = "100%"
private const val MIN_CONTENT = "min-content"

@Route("")
@UIScope
@PageTitle("Qr Generator")
@Menu(order = 0.0)
class QrGenViewView @Autowired constructor(
    val codeGeneratorService: CodeGeneratorService
) : Composite<VerticalLayout?>() {

    init {
        val headerLayoutRow = HorizontalLayout()
        val headerLayoutCol = VerticalLayout()
        val avatar = Avatar()
        val layoutColumn = VerticalLayout()
        val h1 = H1()
        val qrTextField = TextField()
        val generateButton = Button()
        val qrGenLayoutRow = HorizontalLayout()
        val qrCodeImage = Image() // Изображение для отображения QR-кода
        content?.width = WIDTH_100_PERCENT
        content?.style?.set("flex-grow", "1")

        headerLayoutRow.add(headerLayoutCol)
        h1.apply { tuneH1(layoutColumn, this) }
        avatar.apply { tuneAvatar(this, headerLayoutCol) }
        qrTextField.apply { tuneTextField(this, layoutColumn) }
        headerLayoutRow.apply { tuneLayoutRow(this, WIDTH_100_PERCENT, MIN_CONTENT, Alignment.CENTER, JustifyContentMode.CENTER) }
        qrGenLayoutRow.apply { tuneLayoutRow(this, WIDTH_100_PERCENT, MIN_CONTENT) }
        layoutColumn.apply { tuneLayoutColumn2(this) }
        generateButton.apply { tuneGenerateButton(this, layoutColumn) }
            .also { it.addClickListener { getGenerateBtnListener(qrTextField, qrCodeImage) } }
        qrCodeImage.apply {
            layoutColumn.setAlignSelf(Alignment.CENTER, this)
            layoutColumn.add(this)
        }

        content?.add(headerLayoutRow)
        content?.add(layoutColumn)
        content?.add(qrGenLayoutRow)
        content?.add(qrCodeImage)
    }

    private fun getGenerateBtnListener(qrTextField: TextField, qrCodeImage: Image) {
        try {
            val text = qrTextField.value
            if (text.isNotBlank()) {
                val qrCodeStream = codeGeneratorService.generateQrCode(text)
                // Явно указываем, что лямбда относится к InputStreamFactory
                val resource = StreamResource("qrcode.png", InputStreamFactory { qrCodeStream })
                qrCodeImage.setSrc(resource)
            }
        } catch (e: Exception) {
            showErrorNotification(e.message ?: "Unexpected error occurred")
        }
    }

    private fun showErrorNotification(message: String) {
        val notification = Notification(message, 5000, Notification.Position.TOP_END) // Время отображения: 5000 мс
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR) // Стилизуем как ошибку
        notification.open()
    }

    private fun tuneLayoutRow(layoutRow: HorizontalLayout, width: String, height: String, alignItems: Alignment? = null,
                              mode: JustifyContentMode? = null) {
        layoutRow.addClassName(LumoUtility.Gap.MEDIUM)
        layoutRow.width = width
        layoutRow.height = height
        if (alignItems != null) layoutRow.alignItems = alignItems
        if (mode != null) layoutRow.justifyContentMode = mode
    }

    private fun tuneLayoutColumn2(layoutColumn2: VerticalLayout) {
        layoutColumn2.width = WIDTH_100_PERCENT
        layoutColumn2.style.set("flex-grow", "1")
        layoutColumn2.justifyContentMode = JustifyContentMode.START
        layoutColumn2.alignItems = Alignment.CENTER
    }

    private fun tuneH1(layoutColumn2: VerticalLayout, h1: H1) {
        layoutColumn2.setAlignSelf(Alignment.CENTER, h1)
        h1.text = "QR code generator"
        h1.width = "max-content"
        layoutColumn2.add(h1)
    }

    private fun tuneAvatar(avatar: Avatar, layoutRow: VerticalLayout) {
        avatar.name = "Roman Surkoff"
        avatar.image = "./icons/logo-big.png"
        avatar.maxHeight = "5em"
        avatar.maxWidth = "5em"
        avatar.height = "100%"
        avatar.width = "100%"
        // Создаем Anchor и оборачиваем в него аватар
        val link = Anchor("https://surkoff.com", avatar)
        link.setTarget("_blank") // Открывает ссылку в новой вкладке
        layoutRow.setAlignSelf(Alignment.CENTER, link)
        layoutRow.add(link)
    }

    private fun tuneTextField(fld: TextField, layoutColumn: VerticalLayout) {
        fld.label = ""
        fld.placeholder= "Enter text for QR Code"
        // Создаем HorizontalLayout для центрирования
        val horizontalLayout = HorizontalLayout()
        horizontalLayout.justifyContentMode = JustifyContentMode.CENTER
        horizontalLayout.alignItems = Alignment.CENTER
        // Добавляем метку и поле ввода в HorizontalLayout
        horizontalLayout.add(fld)
        // Центрируем HorizontalLayout в VerticalLayout
        layoutColumn.setAlignSelf(Alignment.CENTER, horizontalLayout)
        layoutColumn.add(horizontalLayout)
        fld.width = MIN_CONTENT
        fld.maxWidth = "350px"
    }

    private fun tuneGenerateButton(btn: Button, layoutColumn: VerticalLayout) {
        btn.text = "Create"
        btn.width = MIN_CONTENT
        btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        layoutColumn.setAlignSelf(Alignment.CENTER, btn)
        layoutColumn.add(btn)
    }
}