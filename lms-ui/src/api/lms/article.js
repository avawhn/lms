import request from '@/utils/request'

// 查询文章列表
export function listArticle(query) {
  return request({
    url: '/lms/article/list',
    method: 'get',
    params: query
  })
}

// 查询文章详细
export function getArticle(id) {
  return request({
    url: '/lms/article/' + id,
    method: 'get'
  })
}

// 新增文章
export function addArticle(data) {
  return request({
    url: '/lms/article',
    method: 'post',
    data: data
  })
}

// 修改文章
export function updateArticle(data) {
  return request({
    url: '/lms/article',
    method: 'put',
    data: data
  })
}

// 删除文章
export function delArticle(id) {
  return request({
    url: '/lms/article/' + id,
    method: 'delete'
  })
}
