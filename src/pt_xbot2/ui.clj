(ns pt-xbot2.ui
  (:require [clojure.java.io :as io])
  (import (java.net URI)
          (java.awt SystemTray PopupMenu MenuItem TrayIcon Desktop)
          (java.awt.event ActionListener)
          (javax.swing ImageIcon)
          (javax.imageio ImageIO)))

(defn- load-icon [name]
  (ImageIcon. (ImageIO/read (io/resource name))))

(defn- add-menu-item [menu title action]
  (let [item (MenuItem. title)]
    (.addActionListener item
                        (reify ActionListener
                          (actionPerformed [this event] (action))))
    (.add menu item)))

(defn init-icon [config]
  (if (SystemTray/isSupported)
    (let [icon-not-configured (load-icon "images/trayNotConfigured.png")
          icon-ready (load-icon "images/trayReady.png")
          icon-running (load-icon "images/trayRunning.png")
          icon-error (load-icon "images/trayError.png")
          popup (PopupMenu.)
          tray (SystemTray/getSystemTray)
          tray-icon (TrayIcon. (.getImage icon-not-configured) "Blah blah" popup)]
      (doto popup
        (add-menu-item "Preferences"
                       #(.browse (Desktop/getDesktop)
                                 (URI. "http://localhost:18080/pref")))
        (add-menu-item "Log"
                       #(.browse (Desktop/getDesktop)
                                 (URI. "http://localhost:18080/log")))
        (.addSeparator)
        (add-menu-item "Exit"
                       #(System/exit 0)))
      (doto tray-icon
        (.setImageAutoSize true)
        (.setToolTip "Blah blah tooltip"))
      (.add tray tray-icon))
    (println "Tray is NOT supported")))
