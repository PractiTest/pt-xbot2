(ns pt-xbot2.ui
  (:require [clojure.java.io :as io]
            [clojure.java.browse :as browse])
  (import (java.net URI)
          (java.awt SystemTray PopupMenu MenuItem TrayIcon TrayIcon$MessageType)
          (java.awt.event ActionListener)
          (javax.swing ImageIcon)
          (javax.imageio ImageIO)))

(defn- load-icon [name]
  (.getImage (ImageIcon. (ImageIO/read (io/resource name)))))

(defonce tray-icons {:not-configured (load-icon "images/trayNotConfigured.png")
                 :ready          (load-icon "images/trayReady.png")
                 :running        (load-icon "images/trayRunning.png")
                 :error          (load-icon "images/trayError.png")})

(defonce tray-icon (atom nil))

(defn- mk-menu-item [title action]
  (doto (MenuItem. title)
    (.addActionListener
     (reify ActionListener
       (actionPerformed [this event] (action))))))

(defn- mk-url [config path]
  (str "http://localhost:" (get-in config [:server :listening-port]) path))

(defn- mk-popup-menu [config]
  (doto (PopupMenu.)
    (.add (mk-menu-item "Preferences"
                        #(browse/browse-url (mk-url config "/pref"))))
    (.add (mk-menu-item "Log"
                        #(browse/browse-url (mk-url config "/log"))))
    (.addSeparator)
    (.add (mk-menu-item "Exit"
                        #(System/exit 0)))))

(defn- mk-tray-icon [type title tooltip menu]
  (doto (TrayIcon. (get tray-icons type) title menu)
    (.setImageAutoSize true)
    (.setToolTip tooltip)))

(defn init-tray [config]
  (if (SystemTray/isSupported)
    (do
      (swap! tray-icon
             (fn [_]
               (mk-tray-icon :not-configured
                             "PractiTest xBot"
                             "PractiTest xBot"
                             (mk-popup-menu config))))
      (.add (SystemTray/getSystemTray) @tray-icon))
    (println "Tray is NOT supported")))

(defn change-tray-icon [type caption message]
  (doto @tray-icon
    (.setImage (get tray-icons type))
    (.displayMessage caption message TrayIcon$MessageType/INFO)))
