# plugin-mail-smtp — Vue UI

Tool-level, i18n-only plugin (`service:mail:smtp`), the SMTP implementation
of the `mail` service. Compiled to `webjars/mail-smtp/vue/`.

The legacy `smtp.js` controller was empty and the node is global config
(mode `NONE`), so this plugin ships only the SMTP parameter labels
(`service:mail:smtp:*`). `requires: ['mail']`.

```bash
npm install && npm run build && npm run lint && npm test
```
