/*
 * Plugin "mail-smtp" — SMTP implementation of plugin-mail.
 *
 * Tool-level, i18n-only plugin (`service:mail:smtp`). The legacy
 * `smtp.js` controller was empty and the node is global config (mode
 * NONE), so this plugin contributes no row features — only the SMTP
 * parameter labels for the auto-rendered config/parameter form.
 */
import { useI18nStore } from '@ligoj/host'
import enMessages from './i18n/en.js'
import frMessages from './i18n/fr.js'
import service from './service.js'

const features = {}

export default {
  id: 'mail-smtp',
  label: 'SMTP',
  requires: ['mail'],
  install() {
    const i18n = useI18nStore()
    i18n.merge(enMessages, 'en')
    i18n.merge(frMessages, 'fr')
  },
  feature(action, ...args) {
    const fn = features[action]
    if (!fn) throw new Error(`Plugin "mail-smtp" has no feature "${action}"`)
    return fn(...args)
  },
  service,
  meta: { icon: 'mdi-email-fast', color: 'indigo-darken-2' },
}

export { service }
