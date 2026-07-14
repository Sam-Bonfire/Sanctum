---
name: Sanctuary Design System
colors:
  surface: '#fcf9f8'
  surface-dim: '#dcd9d9'
  surface-bright: '#fcf9f8'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f6f3f2'
  surface-container: '#f0eded'
  surface-container-high: '#eae7e7'
  surface-container-highest: '#e4e2e1'
  on-surface: '#1b1c1c'
  on-surface-variant: '#414844'
  inverse-surface: '#303030'
  inverse-on-surface: '#f3f0f0'
  outline: '#727973'
  outline-variant: '#c1c8c2'
  surface-tint: '#446554'
  primary: '#416352'
  on-primary: '#ffffff'
  primary-container: '#5a7c6a'
  on-primary-container: '#f5fff7'
  inverse-primary: '#aacfba'
  secondary: '#5e5e5b'
  on-secondary: '#ffffff'
  secondary-container: '#e1dfdb'
  on-secondary-container: '#63635f'
  tertiary: '#605b53'
  on-tertiary: '#ffffff'
  tertiary-container: '#79746b'
  on-tertiary-container: '#fffbff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#c6ebd5'
  primary-fixed-dim: '#aacfba'
  on-primary-fixed: '#002114'
  on-primary-fixed-variant: '#2c4d3d'
  secondary-fixed: '#e4e2dd'
  secondary-fixed-dim: '#c8c6c2'
  on-secondary-fixed: '#1b1c19'
  on-secondary-fixed-variant: '#474744'
  tertiary-fixed: '#e9e1d6'
  tertiary-fixed-dim: '#cdc5bb'
  on-tertiary-fixed: '#1e1b15'
  on-tertiary-fixed-variant: '#4b463e'
  background: '#fcf9f8'
  on-background: '#1b1c1c'
  surface-variant: '#e4e2e1'
typography:
  display-scripture:
    fontFamily: Playfair Display
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 42px
    letterSpacing: -0.01em
  headline-lg:
    fontFamily: Playfair Display
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
  headline-lg-mobile:
    fontFamily: Playfair Display
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  body-scripture:
    fontFamily: Playfair Display
    fontSize: 20px
    fontWeight: '400'
    lineHeight: 32px
  body-ui:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-caps:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.08em
  label-sm:
    fontFamily: Inter
    fontSize: 13px
    fontWeight: '500'
    lineHeight: 18px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  unit: 8px
  container-margin: 24px
  element-gap: 16px
  section-gap: 40px
  touch-target: 44px
---

## Brand & Style
The design system is centered on the concept of a "Digital Sanctuary." It aims to evoke a sense of tranquility, reverence, and focused presence, transforming the screen into a quiet space for spiritual reflection. 

The aesthetic is a refined blend of **High-End iOS Minimalism** and **Organic Texturalism**. It prioritizes generous whitespace—often referred to here as "breathing room"—to reduce cognitive load and visual noise. The experience should feel premium, intentional, and calm, moving away from the frantic pace of traditional social applications toward a rhythm of slow, meaningful engagement.

## Colors
The palette is inspired by natural materials and ancient manuscripts.

*   **Primary (Sage Green):** Used for active states, primary actions, and brand moments. It represents growth and peace.
*   **Secondary (Soft Cream):** The foundational surface color. It is warmer and more restful for the eyes than pure white, mimicking high-quality paper or parchment.
*   **Tertiary (Warm Sand):** Used for subtle dividers, secondary containers, and disabled states.
*   **Neutral (Deep Charcoal):** Reserved for typography and iconography to ensure high legibility without the harshness of pure black.

For accessibility, use the Deep Charcoal for all scripture text against the Soft Cream background to maintain a high contrast ratio while preserving the "serene" aesthetic.

## Typography
This design system employs a dual-font strategy to distinguish between content and navigation.

*   **Scripture & Headings:** Use **Playfair Display**. This graceful serif font provides the necessary weight and tradition for sacred texts, making the reading experience feel editorial and significant.
*   **Interface & Controls:** Use **Inter**. A clean, neutral sans-serif that ensures clarity for functional elements, settings, and metadata.

**Hierarchy Note:** Use `body-scripture` for the main reading experience. The increased line height (32px) is critical for long-form reading comfort and spiritual focus.

## Layout & Spacing
The layout follows a **fluid grid** model optimized for mobile-first consumption. 

1.  **Margins:** A generous 24px side margin is mandatory to prevent content from feeling crowded.
2.  **Rhythm:** Use an 8px base unit. Most vertical spacing should be 16px (small), 24px (medium), or 40px (large section breaks).
3.  **Centered Focus:** Scripture reading views should utilize a maximum content width of 600px on tablets to maintain an optimal line length, centered on the screen with wide "gutter" areas to mimic the look of a printed book.

## Elevation & Depth
Depth is expressed through **Ambient Shadows** and **Tonal Layering** rather than heavy borders.

*   **Surface Layers:** The base layer is the Soft Cream. Secondary cards use a slightly lighter tint or a very thin 0.5px stroke of the Tertiary color.
*   **Shadows:** Use extremely diffused, low-opacity shadows (e.g., Blur: 20px, Y-Offset: 4px, Opacity: 4% Charcoal). Shadows should feel like the card is floating gently off the surface, not stuck to it.
*   **iOS Influence:** Navigation bars and bottom sheets should utilize a background blur (Backdrop Filter) with a subtle cream tint to provide context of the content beneath while maintaining focus.

## Shapes
The shape language is organic and approachable. 

*   **Standard Cards & Buttons:** Use a 16px (`rounded-lg`) radius.
*   **Small Elements (Chips/Tags):** Use a 100px (`pill`) radius.
*   **Input Fields:** Match the 16px radius of buttons to maintain a consistent visual language.
*   **Image Masks:** Images should always have rounded corners to avoid "sharp" interruptions in the serene flow of the UI.

## Components

### Buttons
*   **Primary:** Filled Sage Green with white or cream text. No heavy shadows; a subtle lift on tap.
*   **Secondary:** Ghost style with a Deep Charcoal thin-stroke border and label.
*   **Tertiary:** Text-only in Sage Green for low-priority actions.

### Cards
Cards are the primary container for scripture "snippets" or prayer prompts. They should have a 16px corner radius, a Soft Cream background, and a 0.5px "Warm Sand" border or a very soft ambient shadow.

### Input Fields
Inputs use a "floating label" style with a 16px radius. The background should be a slightly darker shade of cream than the main page to provide a clear hit area.

### Icons
Icons must be **thin-stroke (1px or 1.5px)**. They should be elegant and simple—avoiding complex metaphors. Use the Deep Charcoal color at 60% opacity for inactive states and Sage Green for active navigation states.

### Scripture Chips
Small, pill-shaped tags used for categorizing scripture (e.g., "Peace," "Hope," "Morning"). Use a light Sage Green tint for the background and dark Sage Green for the text.

### Progress Indicators
For reading plans, use a thin, elegant line rather than a thick bar. The "active" portion should be Sage Green, and the "track" should be a very light Warm Sand.