![NTM Additions](res/ntmadditions_logo.png)

# NTM Additions

**A small quality-of-life addon for [NTM - Continuum](https://github.com/Warfactory-Official/NTM-Continuum) (HBM's Nuclear Tech Mod for NeoForge 1.21.1).**

It adds Curios slots for NTM's detectors and gas masks, so they work from the slot without taking up hotbar or inventory space. It also adds a Lung Tester and fixes a HUD overlay bug.

---

## ✨ Features

### 🎒 Curios slots
Three new Curios slots, each with its own icon:

| Slot | Accepts | What works from the slot |
|---|---|---|
| **Geiger** | Geiger Counter, Dosimeter | Radiation clicks **and** the radiation HUD |
| **Pollution Detector** | Pollution Detector | Soot / Poison / Heavy Metal readouts |
| **Gas Mask** | Gas Mask, M65-Z, Half Mask, Leather Gas Mask, Trench Mask, Crude Protective Mask | Full hazard protection, and the filter wears down normally |

- A mask in the Curios slot **adds its protection to the protection of your helmet**.
- The filter in the Curios mask is only used when the helmet isn't already filtering.
- Full-set armor with a built-in Geiger counter won't click twice if a counter is also in the slot.
- To install a filter, right-click with the mask while holding the filter in your other hand, **before** you put the mask in the slot.

### 🫁 Lung Tester
A handheld diagnostic tool, ported from the *Lung Diagnostic* in **HBM Extended**:

- **Right-click** to scan your own lungs, or right-click another player or mob to scan theirs.
- Shows **Lung Health [Asbestos]**, **Lung Health [Coal]** (black lung), **Total Lung Health** and an **MKU test**. If the test is positive, it also shows the time left.
- The readouts are color-coded from healthy green to critical red.
- It has its own page in NTM's **F1 Quick Manual**: hover over the item and press F1.

**Recipe:**
```
A F A     A = Aluminium Ingot
A S A     F = Gas Mask Filter
A C A     S = Asbestos Ingot
          C = Analog Circuit
```

### 🥽 Visor overlay options and fix
- **Config option to hide** NTM's fullscreen helmet / gas mask visor overlay.
- **Config option** to show the visor overlay for the mask worn in the Curios slot (on by default).
- **Fix:** the visor overlay no longer hides HUD elements that are drawn after it. Before this fix, the **Jade** tooltip disappeared while wearing helmets like the PaA Battle Hazmat Helmet.

Both options are in **Mods → NTM Additions → Config**, or in `config/ntmadditions-client.toml`.

### 🌐 Languages
English and Brazilian Portuguese (pt_br).

---

## 📦 Requirements

- **Minecraft 1.21.1** with **NeoForge**
- **[NTM - Continuum](https://github.com/Warfactory-Official/NTM-Continuum)**, plus its dependency **Golem Core**
- **[Curios API](https://www.curseforge.com/minecraft/mc-mods/curios)** 9.x

**Install it on both the server and the client.**

---

## ❓ FAQ

**Can I add more items to the slots?**
Yes. Each slot uses an item tag (`curios:geiger`, `curios:pollution`, `curios:mask`), so you can add items with a datapack or KubeJS. An item will only *do* something from the slot if it works like the items above.

**Does this change NTM's balance?**
No. Masks protect exactly as much as they do on your head, and filters wear down at the same rate. The detectors give the same readings.

**Is this an official NTM addon?**
No. It's an unofficial addon and isn't affiliated with the NTM - Continuum team. Please report bugs with this addon here, not on NTM's issue tracker.

---

## 🙏 Credits

- **HbMinecraft** and the HBM's Nuclear Tech Mod team, for the original mod
- The **NTM - Continuum** team (Warfactory), for the NeoForge 1.21.1 port
- **Alcater** and **HBM Extended**, for the original Lung Diagnostic that the Lung Tester is based on
- **TheIllusiveC4**, for the Curios API

---

### 🇧🇷 Português

**NTM Additions** é um addon para o **NTM - Continuum** (NeoForge 1.21.1). Ele adiciona:

- **Slots do Curios** para o Contador Geiger e o Dosímetro (com cliques e HUD de radiação funcionando), para o Detector de Poluição e para máscaras de gás (com proteção completa e desgaste do filtro).
- **Testador de Pulmão**, baseado no Lung Diagnostic do HBM Extended. Mostra a saúde do pulmão contra asbesto e contra carvão, a saúde total e o teste de MKU, e tem uma página no guia do F1.
- **Opção para esconder o overlay** de visor dos capacetes e máscaras.
- **Correção do Jade**, que sumia ao usar capacetes com visor.

Requer NTM - Continuum, Golem Core e Curios. Precisa ser instalado no servidor e no cliente.

---

## 🔨 Building
See `build.sh`. It compiles with plain `javac` (Java 21) against the jars of a NeoForge 1.21.1 instance and packs the jar.

## 📄 License
MIT
