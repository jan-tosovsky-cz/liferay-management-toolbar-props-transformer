module.exports = {
    build: {
        bundler: {
            config: {
                imports: {
                    'frontend-js-web': {
                        '/': '*',
                    }
                }
            }
        }
    }
}
