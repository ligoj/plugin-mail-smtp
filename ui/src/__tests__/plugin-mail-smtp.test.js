import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useI18nStore } from '@ligoj/host'
import def from '../index.js'

beforeEach(() => { setActivePinia(createPinia()) })

describe('plugin-mail-smtp contract', () => {
  it('exposes a valid i18n-only tool manifest', () => {
    expect(def.id).toBe('mail-smtp')
    expect(def.requires).toEqual(['mail'])
    expect(def.routes).toBeUndefined()
    expect(def.service).toBeTypeOf('object')
    expect(def.meta).toMatchObject({ icon: expect.any(String), color: expect.any(String) })
  })
  it('merges SMTP parameter i18n on install', () => {
    const i18n = useI18nStore()
    def.install()
    expect(i18n.t('service:mail:smtp:host')).toBe('Host')
    expect(i18n.t('service:mail:smtp:port')).toBe('Port')
  })
  it('feature() throws for any action (legacy controller was empty)', () => {
    expect(() => def.feature('renderFeatures')).toThrow(/no feature "renderFeatures"/)
    expect(() => def.feature('nope')).toThrow(/no feature "nope"/)
  })
})
