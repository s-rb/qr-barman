package com.surkoff.qr_barman

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@SpringBootApplication
@PWA(name = "QR code scanner and generator", shortName = "QR Barman")
@Theme("my-theme")
open class Application : AppShellConfigurator

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
