module.exports = {
  chainWebpack: config => {
    config
        .plugin('html')
        .tap(args => {
          args[0].title = "Northern Lights Library";
          return args;
        })
  }
}